package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import totoro.yui.util.F


class RipAction : SensitivityAction("rip", "rippo", "ripped") {
    private val maxRandomRips = 5
    private val maxTotalRips = 15

    override fun handle(client: IRCClient, command: Command): Boolean {
        val fromArgs = command.args.firstOrNull()?.toIntOrNull()
        val quantity = fromArgs ?: Yui.Random.nextInt(maxRandomRips) + 1
        when {
            quantity > maxTotalRips -> client.send(command.chan, "${F.Gray}it's a whole graveyard!${F.Reset}")
            else -> client.send(command.chan, Dict.Rip(quantity).joinToString(" "))
        }
        return true
    }
}
