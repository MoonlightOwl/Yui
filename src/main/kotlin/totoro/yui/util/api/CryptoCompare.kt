package totoro.yui.util.api

import com.beust.klaxon.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.Currency

object CryptoCompare {
    fun get(code: String, currency: String?, success: (Currency) -> Unit, failure: () -> Unit) {
        "https://min-api.cryptocompare.com/data/price?fsym=$code&tsyms=USD${ if (currency != null) "," + currency else "" }"
                .httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    val currency = currency ?: "USD"
                    val rate = json.double(currency)
                    if (rate == null) failure()
                    else success(Currency(currency, rate))
                }
            }
        }
    }
}
