package totoro.yui.util.api

import com.beust.klaxon.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.WikiArticle
import java.net.URLEncoder

object Wiki {
    private val charset = "UTF-8"

    fun search(request: String, success: (WikiArticle) -> Unit, failure: () -> Unit) {
        ("https://en.wikipedia.org/w/api.php?action=query&generator=search&gsrsearch=${URLEncoder.encode(request, charset)}" +
            "&gsrlimit=1&prop=extracts|info&inprop=url&exintro&explaintext&exchars=400&format=json&utf8=true")
                .httpGet().responseString { _, _, result ->
                    when (result) {
                        is Result.Failure -> failure()
                        is Result.Success -> {
                            val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                            val query = json.obj("query")
                            val pages = query?.obj("pages")
                            if (pages != null && pages.keys.isNotEmpty()) {
                                val article = pages.obj(pages.keys.first())
                                val title = article?.string("title")
                                val url = article?.string("fullurl")
                                val snippet = article?.string("extract")
                                if (title != null && url != null && snippet != null)
                                    success(WikiArticle(title, url, snippet))
                                else failure()
                            } else failure()
                        }
                    }
                }
    }
}
