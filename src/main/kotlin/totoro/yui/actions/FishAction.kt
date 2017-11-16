package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

private val fishes = Dict.of("-fish", "><>", "<><", "<>><", "><>>", "---E", "><*>", "<*><")

class FishAction : SensitivityAction("fish", "-fish") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan, fishes(Yui.Random.nextInt(7)).joinToString(" "))
        return true
    }
}
