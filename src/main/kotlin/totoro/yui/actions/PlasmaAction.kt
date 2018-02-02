package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F

class PlasmaAction : SensitivityAction("plasma") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan,
            F.Bold + command.content
                   .map { F.mix(F.Black, F.colors[Yui.Random.nextInt(F.colors.size - 2) + 2]) + it }
                   .joinToString("") + F.Reset
        )
        return true
    }
}
