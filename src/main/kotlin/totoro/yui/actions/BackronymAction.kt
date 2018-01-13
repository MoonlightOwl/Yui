package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Backronym

class BackronymAction : SensitivityAction("ba", "back", "backronym", "ac", "acr", "acronym") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (!command.args.isEmpty()) {
            val word = command.args.first()
            Backronym.generate(
                    word,
                    {
                        client.send(command.chan, F.Yellow + word.toUpperCase() + F.Reset + ": " + it.map { char ->
                              if (char.isUpperCase()) F.Bold + char + F.Reset else char.toString()
                        }.joinToString(""))
                    },
                    {
                        client.send(command.chan, F.Gray + "cannot think of anything funny" + F.Reset)
                    }
            )
        } else {
            client.send(command.chan, F.Gray + "gimme a word" + F.Reset)
        }
        return true
    }
}
