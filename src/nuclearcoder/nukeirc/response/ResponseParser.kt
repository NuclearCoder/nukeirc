package nuclearcoder.nukeirc.response

import nuclearcoder.nukeirc.impl.response.ReponseParserImpl

/**
 * Created by NuclearCoder on 2018-01-29.
 */

object ResponseParser {

    fun parseMessage(message: String): Response = ReponseParserImpl.parse(message)

}