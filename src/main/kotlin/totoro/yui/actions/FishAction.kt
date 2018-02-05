package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import totoro.yui.util.F

class FishAction : SensitivityAction("fish", "-fish") {
    private val maxRandomFishes = 5
    private val maxTotalFishes = 20
    private val fishes = Dict.of("><>", "<><", "<>><", "><>>", "---E", "><*>", "<*><")

    override fun handle(client: IRCClient, command: Command): Boolean {
        val quantity =
            if (command.args.isNotEmpty()) command.args.first().toIntOrNull()
            else Yui.Random.nextInt(maxRandomFishes) + 1
        when {
            quantity == null ->  client.send(command.chan, "${F.Gray}what kind of fish is it?${F.Reset}")
            quantity > maxTotalFishes -> client.send(command.chan, "${F.Gray}it's a whole herd!${F.Reset}")
            else -> client.send(command.chan, fishes(quantity).joinToString(" "))
        }
        return true
    }
}
