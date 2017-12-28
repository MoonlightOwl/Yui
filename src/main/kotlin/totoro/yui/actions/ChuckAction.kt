package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Chuck

class ChuckAction : SensitivityAction("chuck", "norris", "chucknorris") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        Chuck.quote({ client.send(command.chan, it) }) {
            client.send(command.chan, F.Gray + "chuck is sleeping" + F.Reset)
        }
        return true
    }
}
