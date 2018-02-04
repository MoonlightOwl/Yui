package totoro.yui.util.api

import org.jsoup.Jsoup
import totoro.yui.Log

object Lua {
    private const val useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/59.0.3071.115 Safari/537.36"

    fun run(code: String): String? {
        return try {
            val result = Jsoup.connect("https://www.lua.org/cgi-bin/demo")
                    .userAgent(useragent)
                    .data("input", code)
                    .post()
                    .select("textarea")
            if (result.isNotEmpty()) result.last().text()
            else null
        } catch (e: Exception) {
            Log.debug(e)
            null
        }
    }
}
