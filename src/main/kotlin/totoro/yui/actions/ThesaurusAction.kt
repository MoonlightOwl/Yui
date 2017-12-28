package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

const val MAX_SYNONYMS = 10

class ThesaurusAction : SensitivityAction("syn", "synonym", "synonyms", "thesaurus", "like", "similar") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val synonyms = Datamuse.thesaurus(word)
            if (synonyms.isNotEmpty())
                client.send(command.chan, F.Yellow + word + F.Reset + ": " +
                        synonyms.take(MAX_SYNONYMS).joinToString(", "))
            else client.send(command.chan, F.Gray + "no synonyms found" + F.Reset)
            true
        } else false
    }
}
