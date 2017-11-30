package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.Datamuse

class PhoneticsAction : SensitivityAction("phonetics", "pronounce", "say",
        "pronunciation", "transcription") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isNotEmpty()) {
            val word = command.args.first()
            val phonetics = Datamuse.phonetics(word)
            if (phonetics != null)
                client.send(command.chan, "\u000308${phonetics.word}\u000F: " +
                        "/${phonetics.phonetics}/")
            else client.send(command.chan, "\u000314cannot find phonetics for this word\u000F")
        } else {
            client.send(command.chan, "gimme a word")
        }
        return true
    }
}
