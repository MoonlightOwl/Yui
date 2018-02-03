package totoro.yui.util.api

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.Comics

object Xkcd {
    fun search(phrase: String, success: (Comics) -> Unit, failure: () -> Unit) {
        "https://relevant-xkcd-backend.herokuapp.com/search"
                .httpPost(listOf("search" to phrase)).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    if (json.boolean("success") == true) {
                        val array = json.array<JsonObject>("results")
                        if (array != null && array.isNotEmpty()) {
                            val item = array.first()
                            val id = item.int("number")
                            val title = item.string("title")
                            val titleText = item.string("titletext")
                            val date = item.string("date")
                            success(Comics(
                                    id ?: -1,
                                    title ?: "noname",
                                    titleText ?: "",
                                    date ?: "never"
                            ))
                        } else failure()
                    } else failure()
                }
            }
        }
    }
}
