package totoro.yui.util

import org.jsoup.Jsoup

object Title {
    private val useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/59.0.3071.115 Safari/537.36"

    fun get(url: String): String? {
        return try {
            val links = Jsoup.connect(url).userAgent(useragent).get().select("title")
            if (links.isNotEmpty()) links.first().text()
            else null
        } catch (e: Exception) {
            null
        }
    }
}
