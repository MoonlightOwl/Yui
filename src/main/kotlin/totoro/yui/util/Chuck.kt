package totoro.yui.util

import com.beust.klaxon.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import org.jsoup.Jsoup

object Chuck {
    private val charset = "UTF-8"

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
