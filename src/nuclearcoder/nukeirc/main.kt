package nuclearcoder.nukeirc

import nuclearcoder.nukeirc.client.ClientListener
import nuclearcoder.nukeirc.client.IrcClient
import nuclearcoder.nukeirc.client.event.ClientConnectedEvent
import nuclearcoder.nukeirc.client.event.ClientErrorEvent
import nuclearcoder.nukeirc.impl.client.NioClient
import nuclearcoder.nukeirc.response.Response
import java.util.logging.Logger

/**
 * Created by NuclearCoder on 2018-01-22.
 */

val LOGGER = Logger.getLogger("NukeIRC")

fun main(args: Array<String>) {

    val client: IrcClient = NioClient("localhost", 6667)

    client.addClientListener(object : ClientListener {
        override fun clientConnected(event: ClientConnectedEvent) {
            LOGGER.info("connected")
        }

        override fun clientError(event: ClientErrorEvent) {
            LOGGER.info("error: " + event.message)
        }

    })

    Thread {
        client.start()
    }.start()

    client.sendResponse(Response("NICK", "nickname"))
    client.sendResponse(Response("USER", "username", "0", "*", longParam = "Full Name"))


}
