package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.Language
import totoro.yui.util.LanguageHelper
import totoro.yui.util.api.BackronymEn
import totoro.yui.util.api.BackronymRu

class BackronymAction : SensitivityAction("ba", "back", "backronym", "ac", "acr", "acronym") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (!command.args.isEmpty()) {
            val word = command.args.first()
            val backronymApi = if (LanguageHelper.detect(word) == Language.RUSSIAN) {
                BackronymRu
            } else {
                BackronymEn
            }
            backronymApi.generate(
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
