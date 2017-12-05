package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.CryptoCompare
import java.text.DecimalFormat


class CryptoCoinAction : SensitivityAction("coin", "btc", "bitcoin", "eth", "ether", "ethereum",
        "doge", "dogecoin", "neo", "neocoin", "monero", "ripple") {

    private val format = DecimalFormat("0.#####################")

    override fun handle(client: IRCClient, command: Command): Boolean {

        fun get(from: String, to: String?) {
            CryptoCompare.get(from.toUpperCase(), to?.toUpperCase(),
                    { currency ->
                        client.send(command.chan,
                                "\u000308${format.format(currency.rate)}\u000F ${currency.code} " +
                                if (currency.description != null) "\u001D(${currency.description})\u000F" else ""
                        )
                    },
                    {
                        client.send(command.chan, "\u000314cannot get the rate for this\u000F")
                    }
            )
        }

        when (command.name) {
            "coin" -> get(command.args.getOrElse(0, { "BTC" }), command.args.getOrNull(1))
            "btc", "bitcoin" -> get("BTC", command.args.firstOrNull())
            "eth", "ether", "ethereum" -> get("ETH", command.args.firstOrNull())
            "doge", "dogecoin" -> get("DOGE", command.args.firstOrNull())
            "neo", "neocoin" -> get("NEO", command.args.firstOrNull())
            "monero" -> get("XMR", command.args.firstOrNull())
            "ripple" -> get("XRP", command.args.firstOrNull())
            else -> client.send(command.chan, "\u000314impossibru!\u000F")
        }

        return true
    }
}
