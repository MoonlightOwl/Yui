package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Chuck

class ChuckAction: Action {
    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "chuck", "norris", "chucknorris" -> {
                    Chuck.quote(
                            { client.send(command.chan, it) },
                            { client.send(command.chan, "chuck is sleeping") }
                    )
                    return null
                }
            }
        }
        return command
    }
}
