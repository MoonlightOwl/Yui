package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class EmptyAction : Action {
    val dict = Dict.Kawaii + Dict.NotSure

    override fun process(client: IRCClient, command: Command) = if (!command.valid) {
        client.send(command.chan, dict())
        null
    } else command
}
