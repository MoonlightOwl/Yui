package totoro.yui.util.api

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.Currency
import java.time.Duration

object CryptoCompare {
    fun get(from: String, to: String?, duration: Duration?, success: (Currency) -> Unit, failure: () -> Unit) {
        "https://min-api.cryptocompare.com/data/price?fsym=$from&tsyms=USD${ if (to != null) "," + to else "" }"
                .httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    val currency = to ?: "USD"
                    val price = json.double(currency)
                    if (price == null) failure()
                    else {
                        if (duration != null) {
                            val (param, amount)  = when {
                                duration.toDays() > 0 -> Pair("day", duration.toDays())
                                duration.toHours() > 0 -> Pair("hour", duration.toHours())
                                else -> Pair("minute", duration.toMinutes())
                            }
                            "https://min-api.cryptocompare.com/data/histo$param?fsym=$from&tsym=$currency&limit=$amount"
                                    .httpGet().responseString { _, _, histoResult ->
                                when (histoResult) {
                                    is Result.Failure -> failure()
                                    is Result.Success -> {
                                        val histoJson = Parser().parse(StringBuilder(histoResult.value)) as JsonObject
                                        val dataArray = histoJson.array<JsonObject>("Data")
                                        if (dataArray != null && dataArray.size > 0) {
                                            val item = dataArray.first()
                                            val openPrice = item.double("open")
                                            if (openPrice != null) {
                                                val percentage = (price - openPrice) / openPrice * 100.0
                                                success(Currency(currency, price, delta = percentage))
                                            } else failure()
                                        } else failure()
                                    }
                                }
                            }
                        } else success(Currency(currency, price))
                    }
                }
            }
        }
    }
}
