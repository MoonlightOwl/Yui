package totoro.yui.util

import org.jsoup.Jsoup
import totoro.yui.Yui
import java.net.URLDecoder
import java.net.URLEncoder

object Google {
    private val url = "http://www.google.com/search?q="
    private val charset = "UTF-8"
    private val useragent = "Yui the Bot v${Yui.Version} (+https://github.com/MoonlightOwl/Yui)"

    fun search(request: String): Pair<String, String>? {
        val links = Jsoup.connect(url + URLEncoder.encode(request, charset))
                .userAgent(useragent).get().select(".g>.r>a")
                .filter { it.absUrl("href").startsWith("http://www.google.ru/url?q=") }  // get rid of images box
        if (links.isNotEmpty()) {
            val first = links.first()
            val title = first.text()
            val rawUrl = first.absUrl("href")
            val url = URLDecoder.decode(rawUrl.substring(rawUrl.indexOf('=') + 1, rawUrl.indexOf('&')), charset)
            return Pair(url, title)
        } else return null
    }
}
