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

    private fun getCurrencyCodes(args: List<String>): Pair<String?, String?> {
        val codes = args.filter { !it[0].isDigit() && it[0] != 'p' }
        return Pair(codes.getOrNull(0), codes.getOrNull(1))
    }
    private fun getDelta(args: List<String>): String? {
        return args.firstOrNull { it[0] == 'p' }
    }
    private fun getAmount(args: List<String>): Double? {
        args.mapNotNull { it.toDoubleOrNull() }.forEach { return it }
        return null
    }

    private fun formatDelta(delta: Double): String {
        return "(" + when {
            delta > 0 -> "\u000303▴\u000F"
            delta < 0 -> "\u000304▾\u000F"
            else -> ""
        } + shortFormat.format(Math.abs(delta)) + "%)"
    }

    override fun handle(client: IRCClient, command: Command): Boolean {

        fun get(from: String, to: String?, range: String?, amount: Double?) {
            val duration = if (range != null)
                try { Duration.parse(range.toUpperCase()) } catch (e: Exception) { null }
            else null

            val finalAmount = amount ?: 1.0

            if (range != null && duration == null) {
                client.send(command.chan, "\u000314try ISO 8601 for time duration\u000F")
            } else {
                CryptoCompare.get(
                        from.toUpperCase(),
                        to?.toUpperCase(),
                        duration,
                        { currency ->
                            client.send(command.chan,
                                    "$finalAmount ${from.toUpperCase()} -> " +
                                    "\u000308${longFormat.format(currency.price * finalAmount)}\u000F ${currency.code}" +
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

        val codes = getCurrencyCodes(command.args)
        val delta = getDelta(command.args)
        val amount = getAmount(command.args)

        when (command.name) {
            "btc", "bitcoin" -> get("BTC", codes.first, delta, amount)
            "eth", "ether", "ethereum" -> get("ETH", codes.first, delta, amount)
            "doge", "dogecoin" -> get("DOGE", codes.first, delta, amount)
            "neo", "neocoin" -> get("NEO", codes.first, delta, amount)
            "monero", "xmr" -> get("XMR", codes.first, delta, amount)
            "ripple" -> get("XRP", codes.first, delta, amount)
            else -> get(codes.first ?: "BTC", codes.second, delta, amount)
        }

        return true
    }
}
