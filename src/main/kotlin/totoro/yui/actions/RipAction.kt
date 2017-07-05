package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class RipAction: Action {
    val dict = Dict.of("rip", "rippo", "rip rip", "rust in peppers", "✝", "✞", "ripped rippo")

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "rip", "rippo" -> {
                    client.send((0..(Yui.Random.nextInt(7))).map { dict() }.joinToString(" "))
                    return null
                }
            }
        }
        return command
    }
}
