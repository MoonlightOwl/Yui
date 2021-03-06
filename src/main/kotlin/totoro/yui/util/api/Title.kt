package totoro.yui.util.api

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.jsoup.Jsoup
import totoro.yui.Log
import java.net.URL

object Title {
    private const val useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/59.0.3071.115 Safari/537.36"

    fun get(url: String): String? {
        return try {
            when {
                url.contains("youtu.be") || url.contains("youtube.com") -> fromYoutube(url)
                        ?: fromHtmlTag(url)
                else -> fromHtmlTag(url)
            }
        } catch (e: Exception) {
            Log.debug(e)
            null
        }
    }

    private fun fromHtmlTag(url: String): String? {
        val links = Jsoup.connect(url).userAgent(useragent).get().select("title")
        return if (links.isNotEmpty()) links.first().text()
        else null
    }

    private fun fromYoutube(url: String): String? {
        return try {
            val raw = URL("http://www.youtube.com/oembed?url=$url&format=json").readText()
            val video = Parser().parse(StringBuilder(raw)) as JsonObject
            "YouTube: " + video.string("title")
        } catch (e: Exception) { Log.debug(e); null }
    }
}
