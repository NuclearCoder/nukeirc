package nuclearcoder.nukeirc.response

/**
 * Created by NuclearCoder on 2018-01-29.
 */

class ResponseParseException(message: String) : Exception("Response parse error: " + message)