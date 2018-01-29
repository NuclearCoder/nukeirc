package nuclearcoder.nukeirc.client

import nuclearcoder.nukeirc.client.event.ClientConnectedEvent
import nuclearcoder.nukeirc.client.event.ClientErrorEvent

/**
 * Created by NuclearCoder on 2018-01-28.
 */

interface ClientListener {

    fun clientConnected(event: ClientConnectedEvent)
    fun clientError(event: ClientErrorEvent)

}