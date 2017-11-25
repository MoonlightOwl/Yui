package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class PjylsAction : SensitivityAction("zog", "pjyls") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan, "　ム\nム　ム")
        return true
    }
}
