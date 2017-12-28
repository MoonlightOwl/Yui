package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.CryptoCompare
import java.text.DecimalFormat
import java.time.Duration


class CoinAction : SensitivityAction("coin", "cur", "currency",
        "btc", "bitcoin", "eth", "ether", "ethereum",
        "doge", "dogecoin", "neo", "neocoin", "monero", "xmr", "ripple") {

    private val longFormat = DecimalFormat("0.#####################")
    private val shortFormat = DecimalFormat("0.###")

    private fun formatDelta(delta: Double): String {
        return "(" + when {
            delta > 0 -> "\u000303▴\u000F"
            delta < 0 -> "\u000304▾\u000F"
            else -> ""
        } + shortFormat.format(Math.abs(delta)) + "%)"
    }

    override fun handle(client: IRCClient, command: Command): Boolean {

        fun get(from: String, to: String?, range: String?) {
            val duration = if (range != null)
                try { Duration.parse(range.toUpperCase()) } catch (e: Exception) { null }
            else null
            if (range != null && duration == null) {
                client.send(command.chan, "\u000314try ISO 8601 for time duration\u000F")
            } else {
                CryptoCompare.get(
                        from.toUpperCase(),
                        to?.toUpperCase(),
                        duration,
                        { currency ->
                            client.send(command.chan,
                                    "1 ${from.toUpperCase()} -> " +
                                    "\u000308${longFormat.format(currency.price)}\u000F ${currency.code}" +
                                    if (currency.description != null) " \u001D(${currency.description})\u000F" else "" +
                                    if (range != null) "  " + formatDelta(currency.delta) else ""
                            )
                        },
                        {
                            client.send(command.chan, "\u000314cannot get the rate for this\u000F")
                        }
                )
            }
        }

        when (command.name) {
            "coin", "cur", "currency" ->
                get(command.args.getOrElse(0, { "BTC" }), command.args.getOrNull(1), command.args.getOrNull(2))
            "btc", "bitcoin" -> get("BTC", command.args.firstOrNull(), command.args.getOrNull(1))
            "eth", "ether", "ethereum" -> get("ETH", command.args.firstOrNull(), command.args.getOrNull(1))
            "doge", "dogecoin" -> get("DOGE", command.args.firstOrNull(), command.args.getOrNull(1))
            "neo", "neocoin" -> get("NEO", command.args.firstOrNull(), command.args.getOrNull(1))
            "monero", "xmr" -> get("XMR", command.args.firstOrNull(), command.args.getOrNull(1))
            "ripple" -> get("XRP", command.args.firstOrNull(), command.args.getOrNull(1))
            else -> client.send(command.chan, "\u000314impossibru!\u000F")
        }

        return true
    }
}
