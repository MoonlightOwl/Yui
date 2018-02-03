package totoro.yui.util.api

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

object Chuck {
    fun quote(success: (String) -> Unit, failure: () -> Unit) {
        "http://api.icndb.com/jokes/random".httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    val quote = json.obj("value")?.string("joke")
                    if (quote != null) success(quote)
                    else failure()
                }
            }
        }
    }
}
