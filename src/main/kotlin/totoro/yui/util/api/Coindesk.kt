package totoro.yui.util.api

import com.beust.klaxon.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.Currency

object Coindesk {
    fun get(currency: String?, success: (Currency) -> Unit, failure: () -> Unit) {
        "https://api.coindesk.com/v1/bpi/currentprice${ if (currency != null) "/" + currency else "" }.json".httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    val bpi = json.obj("bpi")
                    val code = currency ?: "USD"
                    val price = bpi?.obj(code)
                    val rate = price?.double("rate_float")
                    val desc = price?.string("description")
                    if (rate == null || desc == null) failure()
                    else success(Currency(code, rate, desc))
                }
            }
        }
    }
}
