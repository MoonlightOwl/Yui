package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Title

class TitleAction: Action {
    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            if (command.words.first().startsWith("http")) {
                val title = Title.get(command.words.first())
                if (title != null) client.send(command.chan, "\u000308[$title]\u000F")
                else client.send(command.chan, "\u000304[this website does not like me :<]\u000F")
                return null
            }
        }
        return command
    }
}
