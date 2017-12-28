package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Datamuse

const val MAX_ADJECTIVES = 10

class DescribeAction : SensitivityAction("desc", "describe", "decsription") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val adjectives = Datamuse.describe(word)
            if (adjectives.isNotEmpty())
                client.send(command.chan, F.Yellow + word + F.Reset + " can be: " +
                        adjectives.take(MAX_ADJECTIVES).joinToString(", "))
            else client.send(command.chan, F.Gray + "no adjectives found" + F.Reset)
            true
        } else false
    }
}
