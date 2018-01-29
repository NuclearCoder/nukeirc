package nuclearcoder.nukeirc.client

/**
 * Created by NuclearCoder on 2018-01-26.
 */

interface IrcClient {

    fun start()
    fun close()

    fun sendRaw(message: String)

    fun addClientListener(listener: ClientListener)
    fun removeClientListener(listener: ClientListener)
    fun removeAllClientListeners()

}