package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.Coindesk

class BitcoinAction : SensitivityAction("btc", "bitcoin") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        Coindesk.get(
                command.args.firstOrNull(),
                { currency ->
                    client.send(command.chan,
                            "\u000308${currency.rate}\u000F ${currency.code} \u001D(${currency.description})\u000F"
                    )
                },
                {
                    client.send(command.chan, "\u000314coindesk does not answer\u000F")
                }
        )
        return true
    }
}
