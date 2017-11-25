package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class UnicodeAction : SensitivityAction("u", "char", "unicode") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isEmpty() || command.args.first().isEmpty()) {
            client.send(command.chan, "gimme a char")
        } else {
            val char = command.args.first()[0]
            val code = char.toInt()
            client.send(
                    command.chan,
                    "U+${Integer.toHexString(code).toUpperCase().padStart(4, '0')}: " +
                            Character.getName(code) + " ($char)"
            )
        }
        return true
    }
}
