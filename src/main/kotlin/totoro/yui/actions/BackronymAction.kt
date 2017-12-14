package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.Backronym

class BackronymAction : SensitivityAction("ba", "back", "backronym", "ac", "acronym") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (!command.args.isEmpty()) {
            val word = command.args.first()
            Backronym.generate(
                    word,
                    {
                        client.send(command.chan, "\u000308${word.toUpperCase()}\u000F: " + it.map { char ->
                              if (char.isUpperCase()) "\u0002$char\u000F" else char.toString()
                        }.joinToString(""))
                    },
                    {
                        client.send(command.chan, "\u000314cannot think of anything funny\u000F")
                    }
            )
        } else {
            client.send(command.chan, "\u000314gimme a word\u000F")
        }
        return true
    }
}
