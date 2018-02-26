package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

class DefinitionAction : SensitivityAction("def", "define", "definition",
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
                client.send(command.chan, F.Yellow + definition.word + F.Reset + ": " +
                        "${definition.partOfSpeech}  " + F.Italic + definition.definition + F.Reset)
            else client.send(command.chan, F.Gray + "no definitions found" + F.Reset)
            true
        } else false
    }
}
