package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Wiki

class WikiAction : SensitivityAction("w", "wiki", "wikipedia", "!^w:..$", "!^wiki:..$", "!^wikipedia:..$") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        when {
            // check if the country prefix is a part of the command itself
            command.name?.contains(':') == true ->
                search(client, command.chan, command.content, command.name.takeLast(2))

            // check if the country prefix is the first word
            (command.args.firstOrNull()?.startsWith(':') == true) &&
            (command.args.first().length == 3) ->
                search(client, command.chan, command.content.drop(4), command.args.first().drop(1))

            else -> search(client, command.chan, command.content, "en")
        }
        return true
    }

    private fun search(client: IRCClient, channel: String, content: String, country: String) {
        Wiki.search(content, country,
                { article ->
                    client.send(channel, F.Yellow + article.title + F.Reset + " / " + article.url)
                    client.send(channel, F.Italic + article.snippet + F.Reset)
                },
                {
                    client.send(channel, F.Gray + "cannot find anything on the topic" + F.Reset)
                }
        )
    }
}
