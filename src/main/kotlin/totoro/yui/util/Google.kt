package totoro.yui.util

import org.jsoup.Jsoup
import totoro.yui.Yui
import java.net.URLDecoder
import java.net.URLEncoder

object Google {
    private val url = "http://www.google.com/search?q="
    private val charset = "UTF-8"
    private val useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/60.0.3112.78 Safari/537.36"

    fun search(request: String): Pair<String, String>? {
        val document = Jsoup.connect(url + URLEncoder.encode(request, charset))
                .userAgent(useragent).get();
        val links = document.select(".rc>.r>a")
        if (links.isNotEmpty()) {
            val first = links.first()
            val title = first.text()
            val rawUrl = first.absUrl("href")
            val url = URLDecoder.decode(rawUrl, charset)
            return Pair(url, title)
        } else return null
    }
}
