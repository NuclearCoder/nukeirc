package nuclearcoder.nukeirc.response

/**
 * Created by NuclearCoder on 2018-01-29.
 */

interface ResponseHandler {

    fun process(message: String)

}