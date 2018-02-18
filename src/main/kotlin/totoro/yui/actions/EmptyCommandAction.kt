package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

/**
 * This action triggers when the bot receives a command promt with nothing else.
 */

class EmptyCommandAction : CommandAction {
    val dict = Dict.Kawaii + Dict.NotSure

    override fun process(client: IRCClient, command: Command) = if (!command.valid) {
        client.send(command.chan, dict())
        null
    } else command
}
