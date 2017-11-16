package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class BroteAction : SensitivityAction("brote") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val text = if (client.isBroteOnline()) "03[online]" else "04[broken]"
        client.send(command.chan, "\u0003$text\u000F")
        return true
    }
}
