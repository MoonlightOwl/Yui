package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Title

class TitleAction : MessageAction {
    private val urlRegex = ".*https?://.*\\..*".toRegex()

    override fun process(client: IRCClient, channel: String, message: String): String? {
        return if (!client.isBroteOnline() && message.contains("http")) {
            val titles = message
                    .split(' ')
                    .filter { it.matches(urlRegex) }
                    .mapNotNull { Title.get(it) }
            titles.forEach { client.send(channel, "${F.Yellow}[ $it ]${F.Reset}") }
            if (titles.isNotEmpty()) null else message
        } else message
    }
}
