package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

const val MAX_RHYMES = 10

class RhymeAction : SensitivityAction("rhyme") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val rhymes = Datamuse.rhyme(word)
            if (rhymes.isNotEmpty())
                client.send(command.chan, F.Yellow + word + F.Reset + ": " +
                        rhymes.take(MAX_RHYMES).joinToString(", "))
            else client.send(command.chan, F.Gray + "no rhymes found" + F.Reset)
            true
        } else false
    }
}
