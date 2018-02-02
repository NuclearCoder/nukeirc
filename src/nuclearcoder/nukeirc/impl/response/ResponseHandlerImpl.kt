package nuclearcoder.nukeirc.impl.response

import nuclearcoder.nukeirc.response.ResponseHandler
import nuclearcoder.nukeirc.response.ResponseParser

/**
 * Created by NuclearCoder on 2018-01-29.
 */

internal class ResponseHandlerImpl : ResponseHandler {

    companion object {
        val REGEX_LINES = ".*?\r\n".toRegex()
    }

    override fun process(message: String) {

        REGEX_LINES.findAll(message).map(MatchResult::value).forEach {
            val response = ResponseParser.parseMessage(it)
            println(response)
        }

    }

}