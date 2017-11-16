package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

abstract class SensitivityAction(private val sensitivities: List<String>) : Action {

    constructor(vararg sensitivities: String) : this(sensitivities.toList())

    override fun process(client: IRCClient, command: Command): Command? {
        val success = command.name?.toLowerCase() in sensitivities && handle(client, command)
        return if (success) null else command
    }

    abstract fun handle(client: IRCClient, command: Command): Boolean
}
