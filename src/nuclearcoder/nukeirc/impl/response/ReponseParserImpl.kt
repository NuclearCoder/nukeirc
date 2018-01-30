package nuclearcoder.nukeirc.impl.response

import nuclearcoder.nukeirc.response.Response
import nuclearcoder.nukeirc.response.ResponseParseException

/**
 * Created by NuclearCoder on 2018-01-29.
 */

internal object ReponseParserImpl {

    //language=RegExp
    private object RegExp {
        // see RFC 2812, message format

        const val user = "(?:[^\\x00\r\n @]+)" // any but NUL CR LF SP @
        const val letter = "\\p{Alpha}"
        const val hexdigit = "\\p{XDigit}"
        const val special = "['\\[\\]`_^{|}]" // set: '[]\`_^{|}
        const val nospcrlfcl = "[^\\x00\r\n :]" // any but NUL CR LF SP :

        const val middle = "(?:$nospcrlfcl(?::|$nospcrlfcl)*)"
        const val trailing = "(?:(?:[: ]|$nospcrlfcl)*)"

        const val shortname = "(?:(?:\\w)*(?:\\w|-)*(?:\\w)*)"
        const val hostname = "(?:$shortname(?:\\.$shortname))"
        const val ip4addr = "(?:\\d{1,3}(?:\\.\\d{1,3}){3})"
        const val ip6addr = "(?:(?:$hexdigit+(?::$hexdigit+){7})|(?:0:0:0:0:0:(?:0|FFFF):$ip4addr))"
        const val hostaddr = "(?:$ip4addr|$ip6addr)"
        const val host = "(?:$hostname|$hostaddr)"

        const val nickname = "(?:(?:$letter|$special)(?:\\w|$special|-){0,8})"

        /** hostname, nickname, user, host */
        const val prefix = "(?::(?:(?<hostname>$hostname)|(?:(?<nickname>$nickname)(?:(?:!(?<user>$user))?@(?<host>$host))?)))"

        /** name, code */
        const val command = "(?<name>$letter+)|(?<code>\\d{3})"

        /** midcl, tailcl, midnocl, tailnocl */
        const val params = "(?:(?<midcl>(?: $middle){0,14})( :(?<tailcl>$trailing))?|(?<midnocl>(?: $middle){14})( (?<tailnocl>$trailing))?)"

        /** prefix, command, params */
        val MESSAGE = "(?<prefix>$prefix )?(?<command>$command)(?<params>$params)?\r\n".toRegex()

    }

    fun parse(text: String): Response {
        val result = RegExp.MESSAGE.matchEntire(text)
                ?: error("Message does not have valid format")
        val groups = result.groups as MatchNamedGroupCollection

        val prefix = parsePrefix(groups)
        val command = parseCommand(groups)
        val params = parseParams(groups)

        return Response(prefix, command, params)
    }

    private fun parsePrefix(groups: MatchNamedGroupCollection) =
            if (groups contains "prefix") {
                when {
                    groups contains "hostname" -> Response.ResponsePrefix.Hostname(groups value "hostname")
                    groups contains "nickname" -> Response.ResponsePrefix.Nickname(groups value "nickname",
                            groups valueOrNull "user",
                            groups valueOrNull "host")
                    else -> error("Invalid prefix")
                }
            } else {
                Response.ResponsePrefix.None
            }

    private fun parseCommand(groups: MatchNamedGroupCollection) =
            when {
                groups contains "name" -> Response.ResponseCommand.Name(groups value "name")
                groups contains "code" -> Response.ResponseCommand.Code((groups value "code").toInt())
                else -> error("Invalid command")
            }

    private fun parseParams(groups: MatchNamedGroupCollection): Response.ResponseParams {
        val params: List<String>
        val lastParam: String?

        if (groups contains "params") {
            val middle: String
            when {
                groups contains "midcl" -> {
                    middle = groups value "midcl"
                    lastParam = groups valueOrNull "tailcl"
                }
                groups contains "midnocl" -> {
                    middle = groups value "midnocl"
                    lastParam = groups valueOrNull "tailnocl"
                }
                else -> error("Invalid params")
            }

            params = if (middle.isNotEmpty()) { // middle starts with one space, skip that
                middle.substring(1).split(' ')
            } else emptyList()
        } else {
            params = emptyList()
            lastParam = null
        }

        return Response.ResponseParams(params, lastParam)
    }


    private fun error(message: String): Nothing = throw ResponseParseException(message)

    private inline infix fun MatchNamedGroupCollection.contains(entry: String) = (get(entry) != null)
    private inline infix fun MatchNamedGroupCollection.valueOrNull(entry: String) = get(entry)?.value
    private inline infix fun MatchNamedGroupCollection.value(entry: String) = get(entry)?.value
            ?: ""

}