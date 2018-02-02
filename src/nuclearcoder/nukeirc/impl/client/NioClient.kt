package nuclearcoder.nukeirc.impl.client

import nuclearcoder.nukeirc.LOGGER
import nuclearcoder.nukeirc.client.event.ClientConnectedEvent
import nuclearcoder.nukeirc.client.event.ClientErrorEvent
import nuclearcoder.nukeirc.response.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.*

/**
 * Created by NuclearCoder on 2018-01-26.
 */

internal class NioClient(host: String, port: Int) : AbstractClient(host, port) {

    companion object {
        private const val BUFFER_SIZE = 512 // max IRC message length
        private val CHARSET = Charsets.US_ASCII
    }

    private val selector = Selector.open()
    private val channel = SocketChannel.open()
    private val buffer = ByteBuffer.allocateDirect(BUFFER_SIZE)

    private val writeQueue = LinkedList<ByteBuffer>()

    private lateinit var thread: Thread

    override fun start() {
        channel.run {
            configureBlocking(false)
            register(selector, SelectionKey.OP_CONNECT)
            connect(InetSocketAddress(host, port))
        }

        thread = Thread.currentThread()
        while (!thread.isInterrupted) {
            selector.select().takeIf { it > 0 } ?: continue

            selector.selectedKeys().iterator().let { it ->
                while (it.hasNext()) {
                    val key = it.next()
                    it.remove()

                    if (!key.isValid) continue

                    when {
                        key.isConnectable -> processConnect(key)
                        key.isReadable -> processRead(key)
                        key.isWritable -> processWrite(key)
                    }
                }
            }
        }

        channel.close()
        selector.close()
    }

    override fun close() {
        thread.interrupt()
    }

    override fun sendRaw(message: String) {
        synchronized(writeQueue) {
            writeQueue.add(ByteBuffer.wrap(message.toByteArray(CHARSET)))
        }
    }

    override fun sendResponse(response: Response) {
        sendRaw(response.buildMessageString())
    }

    // process keys

    private fun processConnect(key: SelectionKey) {
        key.socketChannel().run {
            if (isConnectionPending) {
                finishConnect()
            }
            configureBlocking(false)
            register(selector, SelectionKey.OP_READ or SelectionKey.OP_WRITE)
        }

        fireClientConnected(ClientConnectedEvent(this))
    }

    private fun processRead(key: SelectionKey) {
        val channel = key.socketChannel()
        buffer.clear()

        val read = try {
            channel.read(buffer)
        } catch (e: IOException) {
            error("Reading error, connection closed")
            return
        }

        if (read == -1) {
            error("End of stream, connection closed")
        } else {
            buffer.flip()

            val message = CHARSET.decode(buffer).toString()

            responseHandler.process(message)
        }
    }

    private fun processWrite(key: SelectionKey) {
        val channel = key.socketChannel()

        synchronized(writeQueue) {
            if (!writeQueue.isEmpty()) {
                val buffer = writeQueue.peek()
                if (buffer.hasRemaining()) {
                    channel.write(buffer)
                } else {
                    writeQueue.remove()
                }
            }
        }
    }

    private fun error(message: String) {
        LOGGER.severe(message)

        fireClientError(ClientErrorEvent(this, message))
        close()
    }

    private inline fun SelectionKey.socketChannel() = channel() as SocketChannel

}