package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.Title

class TitleAction : Action {
    override fun process(client: IRCClient, command: Command): Command? {
        var hit = false
        for (word in command.words) {
            if (word.startsWith("http")) {
                val text = Title.get(word)?.let {
                    F.Yellow + "[ $it ]"
                } ?: F.Red + "[ this website does not like me :< ]"
                client.send(command.chan, text + F.Reset)
                hit = true
            }
        }
        return if (hit) null else command
    }
}
