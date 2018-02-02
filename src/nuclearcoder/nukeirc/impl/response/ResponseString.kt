package nuclearcoder.nukeirc.impl.response

import nuclearcoder.nukeirc.response.Response

/**
 * Created by NuclearCoder on 2018-01-30.
 */

internal object ResponseString {

    fun build(response: Response): String {
        val builder = StringBuilder()
        builder.buildPrefix(response.prefix)
        builder.buildCommand(response.command)
        builder.buildParams(response.params)
        builder.append("\r\n")
        return builder.toString()
    }

    private fun StringBuilder.buildPrefix(prefix: Response.ResponsePrefix) {
        when (prefix) {
            is Response.ResponsePrefix.Hostname -> {
                append(':')
                append(prefix.hostname)
                append(' ')
            }
            is Response.ResponsePrefix.Nickname -> {
                append(':')
                append(prefix.nickname)
                prefix.user?.let {
                    append('!')
                    append(it)
                    prefix.host?.let {
                        append('@')
                        append(it)
                    }
                }
                append(' ')
            }
        }
    }

    private fun StringBuilder.buildCommand(command: Response.ResponseCommand) {
        when (command) {
            is Response.ResponseCommand.Code -> append(command.code)
            is Response.ResponseCommand.Name -> append(command.name)
        }
    }

    private fun StringBuilder.buildParams(params: Response.ResponseParams) {
        if (params.params.isNotEmpty()) {
            append(' ')
            params.params.joinTo(this, separator = " ")
        }

        params.longParam?.let {
            append(" :")
            append(it)
        }
    }

}