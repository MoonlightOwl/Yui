package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Datamuse

const val MAX_ANTONYMS = 10

class AntonymsAction : SensitivityAction("ant", "antonym", "antonyms", "opposite", "unlike") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val antonyms = Datamuse.antonyms(word)
            if (antonyms.isNotEmpty())
                client.send(command.chan, "\u000308$word\u000F: " +
                        antonyms.take(MAX_ANTONYMS).joinToString(", "))
            else client.send(command.chan, "\u000314no antonyms found\u000F")
            true
        } else false
    }
}
