package nuclearcoder.nukeirc.response

import nuclearcoder.nukeirc.impl.response.ResponseString

/**
 * Created by NuclearCoder on 2018-01-29.
 */

data class Response(
        val prefix: ResponsePrefix = ResponsePrefix.None,
        val command: ResponseCommand,
        val params: ResponseParams
) {

    constructor(command: String, vararg params: String, longParam: String)
            : this(command = ResponseCommand.Name(command), params = ResponseParams(params.toList(), longParam))

    constructor(command: String, vararg params: String)
            : this(command = ResponseCommand.Name(command), params = ResponseParams(params.toList(), null))


    sealed class ResponsePrefix {
        object None : ResponsePrefix() {
            override fun toString() = "None"
        }

        data class Hostname(val hostname: String) : ResponsePrefix()

        data class Nickname(val nickname: String,
                            val user: String?,
                            val host: String?) : ResponsePrefix()
    }

    sealed class ResponseCommand {
        data class Name(val name: String) : ResponseCommand()
        data class Code(val code: Int) : ResponseCommand()
    }

    data class ResponseParams(val params: List<String>, val longParam: String?)

    fun buildMessageString(): String = ResponseString.build(this)

}