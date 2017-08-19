package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict


class RipAction : SensitivityAction("rip", "rippo", "ripped") {
    companion object {
        val RipDict = Dict.of("rip", "rippo", "rip rip", "rust in peppers", "✝", "✞", "ripped rippo")
    }

    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan, RipDict(Yui.Random.nextInt(7)).joinToString(" "))
        return true
    }
}
