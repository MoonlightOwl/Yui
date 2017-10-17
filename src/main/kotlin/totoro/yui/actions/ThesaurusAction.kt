package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Datamuse

const val MAX_SYNONYMS = 10

class ThesaurusAction : SensitivityAction("syn", "synonym", "synonyms", "thesaurus") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val synonyms = Datamuse.thesaurus(word)
            if (synonyms.isNotEmpty())
                client.send(command.chan, "\u000308$word\u000F: " +
                        synonyms.take(MAX_SYNONYMS).joinToString(", "))
            else client.send(command.chan, "\u000314no synonyms found\u000F")
            true
        } else false
    }
}
