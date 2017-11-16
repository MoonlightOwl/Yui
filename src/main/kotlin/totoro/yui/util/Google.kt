package totoro.yui.util

import org.jsoup.Connection
import org.jsoup.Jsoup
import totoro.yui.Log
import java.net.URLDecoder
import java.net.URLEncoder

object Google {
    private val charset = "UTF-8"
    private val useragent = "Mozilla/5.0 (X11; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0"

    private var cookies: Map<String, String>? = null

    private fun url(request: String): String {
        return "https://www.google.com/search?ie=utf-8&hl=en&source=hp&gbv=1" +
                "&q=$request&safe=disabled"
    }

    fun search(request: String): Pair<String, String>? {
        // generate connect object
        val connect = Jsoup.connect(url(URLEncoder.encode(request, charset)))
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .userAgent(useragent)

        // if no cookies in the jar - then try to get 'em
        if (cookies == null) {
            val response = connect.method(Connection.Method.GET).execute()
            cookies = response.cookies()
            Log.info("Successfully got cookies for this Google session...")
        }

        // search
        val document = connect.cookies(cookies).get()
        val links = document.select(".g>.r>a")
        return if (links.isNotEmpty()) {
            val first = links.first()
            val title = first.text()
            val rawUrl = first.absUrl("href")
            val url = URLDecoder.decode(rawUrl, charset)
            Pair(url.substring(url.indexOf('=') + 1, url.indexOf("&sa")), title)
        } else null
    }
}
