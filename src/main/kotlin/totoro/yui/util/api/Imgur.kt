package totoro.yui.util.api

import org.jsoup.Jsoup
import totoro.yui.util.api.data.Picture

object Imgur {
    private const val useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/59.0.3071.115 Safari/537.36"

    fun random(): Picture? {
        return try {
            val doc = Jsoup.connect("https://imgur.com/random").userAgent(useragent).get()
            Picture(doc.title(), doc.select("link[rel=image_src]").first().attr("href"))
        } catch (e: Exception) {
            null
        }
    }
}
