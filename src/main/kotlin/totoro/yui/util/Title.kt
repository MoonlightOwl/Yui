package totoro.yui.util

import org.jsoup.Jsoup
import totoro.yui.Yui

object Title {
    private val useragent = "Yui the Bot v${Yui.Version} (+https://github.com/MoonlightOwl/Yui)"

    fun get(url: String): String? {
        try {
            val links = Jsoup.connect(url).userAgent(useragent).get().select("title")
            if (links.isNotEmpty()) return links.first().text()
            else return null
        } catch (e: Exception) {
            return null
        }
    }
}
