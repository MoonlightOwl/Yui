package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F

class BroteAction : SensitivityAction("brote") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val text = if (client.isBroteOnline()) F.Green + "[online]" else F.Red + "[broken]"
        client.send(command.chan, text + F.Reset)
        return true
    }
}
