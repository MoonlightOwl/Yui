package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Datamuse

const val MAX_ADJECTIVES = 10

class DescribeAction : SensitivityAction("desc", "describe", "decsription") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        return if (command.args.isNotEmpty()) {
            val word = command.args.joinToString(" ")
            val adjectives = Datamuse.describe(word)
            if (adjectives.isNotEmpty())
                client.send(command.chan, "\u000308$word\u000F can be: " +
                        adjectives.take(MAX_ADJECTIVES).joinToString(", "))
            else client.send(command.chan, "\u000314no adjectives found\u000F")
            true
        } else false
    }
}
