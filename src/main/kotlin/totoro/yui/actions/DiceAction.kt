package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class DiceAction : SensitivityAction("dice") {
    private val maxAmount = 50
    private val faces = Dict.of("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

    override fun handle(client: IRCClient, command: Command): Boolean {
        val amount = if (command.args.isNotEmpty()) {
            command.args.first().toIntOrNull() ?: 1
        } else 1
        client.send(command.chan, (1..Math.min(maxAmount, amount)).joinToString(" ") { faces() })
        return true
    }
}
