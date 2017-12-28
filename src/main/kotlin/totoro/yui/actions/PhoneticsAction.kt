package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

class PhoneticsAction : SensitivityAction("phonetics", "pronounce", "say",
        "pronunciation", "transcription") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isNotEmpty()) {
            val word = command.args.first()
            val phonetics = Datamuse.phonetics(word)
            if (phonetics != null)
                client.send(command.chan, F.Yellow + phonetics.word + F.Reset + ": " +
                        "/${phonetics.phonetics}/")
            else client.send(command.chan, F.Gray + "cannot find phonetics for this word" + F.Reset)
        } else {
            client.send(command.chan, F.Gray + "gimme a word" + F.Reset)
        }
        return true
    }
}
