package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Title

class TitleAction : Action {
    private val urlRegex = ".*https?://.*\\..*".toRegex()

    override fun process(client: IRCClient, command: Command): Command? {
        command.words
                .filter { it.matches(urlRegex) }
                .mapNotNull { Title.get(it) }
                .forEach { client.send(command.chan, "${F.Yellow}[ $it ]${F.Reset}") }
        // always return null, because this action takes unprefixed strings,
        // and must be the last in the chain of `unprefixed` actions
        return null
    }
}
