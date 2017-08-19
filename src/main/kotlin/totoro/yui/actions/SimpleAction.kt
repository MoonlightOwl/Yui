package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class SimpleAction(activators: List<String>, private val response: Dict<String>) : SensitivityAction(activators) {
    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan, response())
        return true
    }
}
