package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Title

/**
 * Searches for URL adresses in the message and then prints the titles
 * of corresponding web pages
 */

class TitleAction : SensitivityAction("title"), MessageAction {
    companion object {
        val instance: TitleAction by lazy { TitleAction() }
    }

    private val urlRegex = ".*https?://.*\\..*".toRegex()

    private fun printAllTitles(phrases: List<String>, channel: String, client: IRCClient): Boolean {
        val titles = phrases.filter { it.matches(urlRegex) }.mapNotNull { Title.get(it) }
        titles.forEach { client.send(channel, "${F.Yellow}[ $it ]${F.Reset}") }
        return titles.isNotEmpty()
    }

    override fun process(client: IRCClient, channel: String, user: String, message: String): String? {
        return if (!client.isBroteOnline() && message.contains("http")) {
            if (printAllTitles(message.split(' '), channel, client)) null
            else message
        } else message
    }

    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isNotEmpty())
            printAllTitles(command.args, command.chan, client)
        return true
    }
}
