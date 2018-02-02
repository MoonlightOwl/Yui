package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class TotoroAction : SensitivityAction("totoro") {
    private val totoro = arrayOf(" _//|", "/oo |", "\\mm_|")

    override fun handle(client: IRCClient, command: Command): Boolean {
        val quantity =
            if (command.args.isEmpty()) 1
            else command.args.first().toIntOrNull() ?: 1
        totoro.map {
            client.send(command.chan, (it + " ").repeat(quantity))
        }
        return true
    }
}
