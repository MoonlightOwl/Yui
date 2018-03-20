package totoro.yui.util.api

import org.jsoup.Jsoup
import totoro.yui.Log

object BackronymRu : BackronymApi {
    private const val useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/59.0.3071.115 Safari/537.36"

    override fun generate(word: String, success: (String) -> Unit, failure: () -> Unit) {
        try {
            val result = Jsoup.connect("http://aiproject.ru/abbr.php")
                    .userAgent(useragent)
                    .data("abbr", word)
                    .post()
                    .select(".main_Item")
            if (result.isNotEmpty()) success(result.first().text().drop(3))
            else failure()
        } catch (e: Exception) {
            Log.debug(e)
            failure()
        }
    }
}
