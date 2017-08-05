package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import java.util.*

class RipOrNotAction : Action {
    private fun check(data: String): String =
        when(Math.abs(data.hashCode() % 3)) {
            0 -> Dict.Yeah()
            1 -> Dict.Maybe()
            2 -> Dict.Nope()
            else -> { println("!!! ${data.hashCode() % 3}"); Dict.Kawaii() }
        }

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            println(command.words.first())
            when (command.words.first()) {
                "rip?", "rippo?", "ripped?" -> {
                    client.send(
                            command.chan,
                            check(command.words.joinToString(" ") + (Date().time / 3600000))
                    )
                    return null
                }
            }
        }
        return command
    }
}
