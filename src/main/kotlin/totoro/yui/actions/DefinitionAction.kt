package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Datamuse

class DefinitionAction : SensitivityAction("d", "def", "define", "definition",
                                          "whatis", "wtfis", "what", "wtf") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val args = (
                if (command.args.size > 1 && (command.name == "wtf" || command.name == "what") &&
                        command.args[0] == "is")
                    command.args.drop(1)
                else
                    command.args
            )
            val partOfSpeech = args.filter { it.startsWith("(") && it.endsWith(")") }.map { it.drop(1).dropLast(1) }
            val filteredArgs = args.filterNot { it.startsWith("(") && it.endsWith(")") }
            val word = filteredArgs.joinToString(" ")
            val definition = Datamuse.definition(word, partOfSpeech)
            if (definition != null)
                client.send(command.chan, "\u000308${definition.word}\u000F: " +
                        "${definition.partOfSpeech}  \u001D${definition.definition}\u000F")
            else client.send(command.chan, "\u000314no definitions found\u000F")
            true
        } else false
    }
}
