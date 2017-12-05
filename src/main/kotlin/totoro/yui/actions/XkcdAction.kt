package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.Xkcd

class XkcdAction : SensitivityAction("x", "xkcd") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        Xkcd.search(
                command.content,
                { comics ->
                    client.send(command.chan,
                            "\u000308${comics.title}\u000F " +
                            "/ https://xkcd.com/${comics.id}/"
                    )
                    client.send(command.chan,
                            "\u001d(" +
                            (if (comics.titleText.length > 350) comics.titleText.take(350) + "..." else comics.titleText) +
                            ")\u000F"
                    )
                },
                {
                    client.send(command.chan, "\u000314no xkcd for this\u000F")
                }
        )
        return true
    }
}
