package nuclearcoder.nukeirc.response

import nuclearcoder.nukeirc.impl.response.ResponseBuilderImpl

/**
 * Created by NuclearCoder on 2018-01-30.
 */

object ResponseBuilder {

    fun build(response: Response): String = ResponseBuilderImpl.buildString(response)

}