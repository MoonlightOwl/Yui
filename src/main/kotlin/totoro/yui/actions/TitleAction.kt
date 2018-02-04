package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Title

class TitleAction : Action {
    private val urlRegex = ".*https?://.*\\..*".toRegex()

    override fun process(client: IRCClient, command: Command): Command? {
        return if (!command.prefixed) {
            val titles =command.words
                    .filter { it.matches(urlRegex) }
                    .mapNotNull { Title.get(it) }

            titles.forEach { client.send(command.chan, "${F.Yellow}[ $it ]${F.Reset}") }

            if (titles.isNotEmpty()) null else command
        } else command
    }
}
