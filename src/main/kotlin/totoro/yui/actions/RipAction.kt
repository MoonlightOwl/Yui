package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict


class RipAction: Action {
    companion object {
        val RipDict = Dict.of("rip", "rippo", "rip rip", "rust in peppers", "✝", "✞", "ripped rippo")
    }

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "rip", "rippo", "ripped" -> {
                    client.send(
                            command.chan,
                            (0..(Yui.Random.nextInt(7))).joinToString(" ") { RipDict() }
                    )
                    return null
                }
            }
        }
        return command
    }
}
