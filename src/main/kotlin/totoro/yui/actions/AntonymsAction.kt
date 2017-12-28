package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

const val MAX_ANTONYMS = 10

class AntonymsAction : SensitivityAction("ant", "antonym", "antonyms", "opposite", "unlike") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val antonyms = Datamuse.antonyms(word)
            if (antonyms.isNotEmpty())
                client.send(command.chan, F.Yellow + word + F.Reset + ": " +
                        antonyms.take(MAX_ANTONYMS).joinToString(", "))
            else client.send(command.chan, F.Gray + "no antonyms found" + F.Reset)
            true
        } else false
    }
}
