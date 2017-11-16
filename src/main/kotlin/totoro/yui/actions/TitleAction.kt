package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Title

class TitleAction : Action {
    override fun process(client: IRCClient, command: Command): Command? {
        var hit = false
        for (word in command.words) {
            if (word.startsWith("http")) {
                val text = Title.get(word)?.let {
                    "08[$it]"
                } ?: "04[this website does not like me :<]"
                client.send(command.chan, "\u0003$text\u000F")
                hit = true
            }
        }
        return if (hit) null else command
    }
}
