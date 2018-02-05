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
        val quantity =
                if (command.args.isNotEmpty()) command.args.first().toIntOrNull()
                else Yui.Random.nextInt(maxRandomRips) + 1
        when {
            quantity == null ->  client.send(command.chan, "${F.Gray}what kind of rip is it?${F.Reset}")
            quantity > maxTotalRips -> client.send(command.chan, "${F.Gray}it's a whole graveyard!${F.Reset}")
            else -> client.send(command.chan, Dict.Rip(quantity).joinToString(" "))
        }
        return true
    }
}
