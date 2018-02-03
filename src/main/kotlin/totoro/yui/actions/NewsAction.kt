package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.HackerNews
import totoro.yui.util.api.data.Story

class NewsAction : SensitivityAction("news", "hn", "hackernews") {
    private val maxNumberOfNews = 10

    override fun handle(client: IRCClient, command: Command): Boolean {
        val filter = command.args.find { it.first().isLetter() }
        val quantity = Math.max(0, Math.min(maxNumberOfNews,
                command.args.find { it.all { char -> char.isDigit() } } ?.toIntOrNull() ?: 1))
        when (filter) {
            "top" -> show(HackerNews::topStory, client, command.chan, quantity)
            "best" -> show(HackerNews::bestStory, client, command.chan, quantity)
            "help" ->
                client.send(command.chan, "use `new` filter (or no filters at all) for fresh news, " +
                                          "or `top` / `best` filters for corresponding lists; " +
                                          "also you can add optional quantity number")
            else -> show(HackerNews::newStory, client, command.chan, quantity)
        }
        return true
    }

    private fun show(getter: ((Story) -> Unit, () -> Unit, () -> Unit) -> Unit, client: IRCClient, channel: String, quantity: Int) {
        getter(
                { story -> client.send(channel, "[${story.score}] " + F.Yellow + story.title + F.Reset + " / " + story.url) },
                { client.send(channel, F.Gray + "for some reasons I cannot show you any news" + F.Reset) },
                { if (quantity > 1) show(getter, client, channel, quantity - 1) }
        )
    }
}
