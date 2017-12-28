package totoro.yui.util.api

import com.beust.klaxon.*
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.Symbol

object UnicodeTable {
    fun get(query: String, success: (List<Symbol>) -> Unit, failure: () -> Unit) {
        "https://unicode-table.com/en/a-search/".httpPost(listOf("s" to query)).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    val data = json.obj("result")
                    val table = data?.array<JsonArray<String>>("c")
                    if (table != null && table.isNotEmpty()) {
                        val list = table.map { item ->
                            val code = "U+" + item[0]
                            val symbol = String(Character.toChars(Integer.parseInt(item[0], 16)))
                            val description = item[1]
                            Symbol(code, symbol, description)
                        }
                        success(list)
                    } else failure()
                }
            }
        }
    }
}
