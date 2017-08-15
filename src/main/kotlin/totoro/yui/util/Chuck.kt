package totoro.yui.util

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.obj
import com.beust.klaxon.string
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import org.jsoup.Jsoup

object Chuck {
    fun quote(success: (String) -> Unit, failure: () -> Unit) {
        "http://api.icndb.com/jokes/random".httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val raw = Jsoup.parse(result.getAs<String>()).text()
                    val json = Parser().parse(StringBuilder(raw)) as JsonObject
                    val quote = json.obj("value")?.string("joke")
                    if (quote != null) success(quote)
                    else failure()
                }
            }
        }
    }
}
