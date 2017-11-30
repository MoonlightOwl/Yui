package totoro.yui.util.api

import com.beust.klaxon.*
import java.net.URL
import java.net.URLEncoder

object YandexSpeller {
    private val charset = "UTF-8"

    fun correct(phrase: String): String {
        var result = phrase
        val raw = URL("http://speller.yandex.net/services/spellservice.json/" +
                "checkText?text=${URLEncoder.encode(phrase, charset)}").readText()
        @Suppress("UNCHECKED_CAST")
        val array = Parser().parse(StringBuilder(raw)) as JsonArray<JsonObject>
        array.forEach { json ->
            val word = json.string("word")
            val variants = json.array<String>("s")
            if (word != null) {
                result = if (variants != null && variants.isNotEmpty())
                    result.replace(word, variants.first(), true)
                else
                    result.replace(word, "<rip>", true)
            }
        }
        return result
    }
}
