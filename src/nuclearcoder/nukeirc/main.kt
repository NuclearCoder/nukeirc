package nuclearcoder.nukeirc

import nuclearcoder.nukeirc.response.ResponseParser
import java.util.logging.Logger

/**
 * Created by NuclearCoder on 2018-01-22.
 */

val LOGGER = Logger.getLogger("NukeIRC")

fun main(args: Array<String>) {

    /*
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

    client.sendRaw("USER username 0 * :Full Name\r\n")
    client.sendRaw("NICK nickname\r\n")
    */

    /*val max = 3

    val str = ":host.host COMMAND word word word :tailblahayiporjketoi"

    val colonstart = str.indexOf(" :")

    val elements = (if (colonstart < 0) str else str.substring(0, colonstart)).split(' ', limit = max+1)

    println(elements.joinToString { "\"$it\"" })

    val endtail = if (colonstart < 0) "" else str.substring(colonstart + 1)
    val tail = if (elements.size == max+1) (elements.last() + ' ' + endtail) else endtail

    println(tail)*/


    val m = ResponseParser.parseMessage("USER user\r\n")
    println(m)


}
