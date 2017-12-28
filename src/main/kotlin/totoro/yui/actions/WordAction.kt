package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

const val MAX_WORDS = 10

class WordAction : SensitivityAction("word", "wordfor", "mean", "means", "guess", "name", "nameit") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val definition = command.args.joinToString(" ")
            val words = Datamuse.word(definition)
            if (words.isNotEmpty())
                client.send(command.chan, F.Yellow + definition + F.Reset + ": " +
                        words.take(MAX_WORDS).joinToString(", "))
            else client.send(command.chan, F.Gray + "no such words found" + F.Reset)
            true
        } else false
    }
}
