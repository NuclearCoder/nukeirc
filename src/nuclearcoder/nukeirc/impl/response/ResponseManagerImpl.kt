package nuclearcoder.nukeirc.impl.response

import nuclearcoder.nukeirc.response.ResponseManager

/**
 * Created by NuclearCoder on 2018-01-29.
 */

class ResponseManagerImpl : ResponseManager {

    override fun process(message: String) {
        val parts = message.split(' ')

        when (parts.first()) {
        }
    }

}