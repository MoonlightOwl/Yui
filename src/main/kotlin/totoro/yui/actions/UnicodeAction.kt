package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class UnicodeAction : SensitivityAction("u", "char", "unicode") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isEmpty() || command.args.first().isEmpty())
            client.send(command.chan, "gimme a char")
        else
            client.send(command.chan, Character.getName(command.args.first()[0].toInt()))
        return true
    }
}
