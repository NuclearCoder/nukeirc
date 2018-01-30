package nuclearcoder.nukeirc.impl.client

import nuclearcoder.nukeirc.client.ClientListener
import nuclearcoder.nukeirc.client.IrcClient
import nuclearcoder.nukeirc.client.event.ClientConnectedEvent
import nuclearcoder.nukeirc.client.event.ClientErrorEvent
import nuclearcoder.nukeirc.impl.response.ResponseHandlerImpl
import nuclearcoder.nukeirc.response.ResponseHandler

/**
 * Created by NuclearCoder on 2018-01-28.
 */

internal abstract class AbstractClient(
        protected val host: String,
        protected val port: Int,
        protected val responseHandler: ResponseHandler = ResponseHandlerImpl()
) : IrcClient {

    private val listeners = mutableListOf<ClientListener>()

    override fun addClientListener(listener: ClientListener) {
        listeners.add(listener)
    }

    override fun removeClientListener(listener: ClientListener) {
        listeners.remove(listener)
    }

    override fun removeAllClientListeners() {
        listeners.clear()
    }

    protected fun fireClientConnected(event: ClientConnectedEvent) {
        listeners.forEach { it.clientConnected(event) }
    }

    protected fun fireClientError(event: ClientErrorEvent) {
        listeners.forEach { it.clientError(event) }
    }

}
