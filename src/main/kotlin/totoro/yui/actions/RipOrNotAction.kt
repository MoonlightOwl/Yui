package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import java.util.*

class RipOrNotAction : SensitivityAction("rip?", "rippo?", "ripped?") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val text = check(command.original + (Date().time / 3600000))
        client.send(command.chan, text)
        return true
    }

    private fun check(data: String): String = when (Math.abs(data.hashCode() % 3)) {
        0 -> Dict.Yeah()
        1 -> Dict.Maybe()
        2 -> Dict.Nope()
        else -> Dict.Kawaii()
    }
}
