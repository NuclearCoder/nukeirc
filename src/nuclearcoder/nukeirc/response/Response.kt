package nuclearcoder.nukeirc.response

/**
 * Created by NuclearCoder on 2018-01-29.
 */

data class Response(
        val prefix: ResponsePrefix,
        val command: ResponseCommand,
        val params: ResponseParams
) {

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

}