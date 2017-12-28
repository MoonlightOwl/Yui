package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Xkcd

class XkcdAction : SensitivityAction("x", "xkcd") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        Xkcd.search(
                command.content,
                { comics ->
                    client.send(command.chan,
                            F.Yellow + comics.title + F.Reset +
                            " / https://xkcd.com/${comics.id}/"
                    )
                    client.send(command.chan,
                            F.Italic + "(" +
                            (if (comics.titleText.length > 350) comics.titleText.take(350) + "..." else comics.titleText) +
                            ")" + F.Reset
                    )
                },
                {
                    client.send(command.chan, F.Gray + "no xkcd for this" + F.Reset)
                }
        )
        return true
    }
}
