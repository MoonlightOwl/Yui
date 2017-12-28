package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
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
                                    "(" + F.Yellow + "${symbol.code}: ${symbol.symbol}" + F.Reset + (if (symbol.symbol.length > 1) "  " else " ") + ")"
                                }
                        )
                    },
                    {
                        client.send(command.chan, F.Gray + "no characters found" + F.Reset)
                    }
            )
        }
        return true
    }
}
