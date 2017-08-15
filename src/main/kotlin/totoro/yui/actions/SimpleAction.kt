package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class SimpleAction(private val activators: List<String>, private val response: Dict<String>) : Action {
    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty() && command.words.first() in activators) {
            client.send(command.chan, response())
            return null
        }
        return command
    }
}
