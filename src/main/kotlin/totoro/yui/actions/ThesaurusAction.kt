package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Datamuse

class ThesaurusAction : SensitivityAction("d", "def", "define", "definition", "thesaurus",
                                          "whatis", "wtfis", "what", "wtf") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = (
                    if (command.args.size > 1 && (command.name == "wtf" || command.name == "what") &&
                            command.args[0] == "is")
                        command.args[1]
                    else
                        command.args[0]
                    )
            val definition = Datamuse.thesaurus(word)
            client.send(command.chan, definition)
            true
        } else false
    }
}
