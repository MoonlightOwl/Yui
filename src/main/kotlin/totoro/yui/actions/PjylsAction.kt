package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class PjylsAction : SensitivityAction("zog", "pjyls") {
    private val zog = Dict.of("　ム\nム　ム", " ▴\n▴ ▴")

    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan, zog())
        return true
    }
}
