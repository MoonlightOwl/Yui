package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.UnicodeTable

class UnicodeAction : SensitivityAction("u", "char", "unicode") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isEmpty()) {
            client.send(command.chan, "gimme some clues")
        } else {
            UnicodeTable.get(
                    command.content,
                    { list ->
                        client.send(
                            command.chan,
                            list
                                .filter { symbol -> symbol.symbol.isNotEmpty() && symbol.description.isNotEmpty() }
                                .joinToString(", ")
                                { symbol ->
                                    "${symbol.description} " +
                                    "(\u000308${symbol.code}: ${symbol.symbol}\u000F${if (symbol.symbol.length > 1) "  " else " "})"
                                }
                        )
                    },
                    {
                        client.send(command.chan, "\u000314no characters found\u000F")
                    }
            )
        }
        return true
    }
}
