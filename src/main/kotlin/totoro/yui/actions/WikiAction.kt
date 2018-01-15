package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Wiki

class WikiAction : SensitivityAction("w", "wiki", "wikipedia") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        Wiki.search(command.content,
                { article ->
                    client.send(command.chan, F.Yellow + article.title + F.Reset + " / " + article.url)
                    client.send(command.chan, F.Italic + article.snippet + F.Reset)
                },
                {
                    client.send(command.chan, F.Gray + "cannot find anything on the topic" + F.Reset)
                }
        )
        return true
    }
}
