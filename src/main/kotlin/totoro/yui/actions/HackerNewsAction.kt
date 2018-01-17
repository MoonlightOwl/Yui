package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.HackerNews
import totoro.yui.util.api.data.Story

class HackerNewsAction : SensitivityAction("news", "hacker", "hn", "hackernews", "hack", "hacknews") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        when (command.args.firstOrNull()) {
            "top" -> show(HackerNews::topStory, client, command.chan)
            "best" -> show(HackerNews::bestStory, client, command.chan)
            "help" ->
                client.send(command.chan, "use `new` filter (or no filters at all) for fresh news, " +
                                          "or `top` / `best` filters for corresponding lists")
            else -> show(HackerNews::newStory, client, command.chan)
        }
        return true
    }

    private fun show(getter: ((Story) -> Unit, () -> Unit) -> Unit, client: IRCClient, channel: String) {
        getter(
            { story -> client.send(channel, "[${story.score}] " + F.Yellow + story.title + F.Reset + " / " + story.url) },
            { client.send(channel, F.Gray + "for some reasons I cannot show you any news" + F.Reset) }
        )
    }
}
