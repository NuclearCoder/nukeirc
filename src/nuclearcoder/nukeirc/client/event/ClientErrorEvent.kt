package nuclearcoder.nukeirc.client.event

import nuclearcoder.nukeirc.client.IrcClient

/**
 * Created by NuclearCoder on 2018-01-29.
 */

class ClientErrorEvent(client: IrcClient,
                       val message: String) : ClientEvent(client)