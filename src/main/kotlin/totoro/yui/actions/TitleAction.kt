package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Title

class TitleAction : Action {
    override fun process(client: IRCClient, command: Command): Command? {
        if (command.name?.startsWith("http") == true) {
            val text = Title.get(command.name)?.let {
                "08[$it]"
            } ?: "04[this website does not like me :<]"
            client.send(command.chan, "\u0003$text\u000F")
            return null
        }
        return command
    }
}
