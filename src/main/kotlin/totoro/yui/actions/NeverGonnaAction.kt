package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class NeverGonnaAction : SensitivityAction("never", "nevergonna") {
    private val promises = Dict.of(
            "... give you up",
            "... let you down",
            "... run around",
            "... desert you",
            "... make you cry",
            "... say goodbye",
            "... tell a lie",
            "... hurt you"
    )

    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.name == "nevergonna" || (command.args.size == 1 && command.args[0] == "gonna")) {
            client.send(command.chan, promises())
            return true
        }
        return false
    }
}
