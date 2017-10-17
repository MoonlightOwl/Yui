package totoro.yui.util

import com.beust.klaxon.*
import java.net.URL
import java.net.URLEncoder

object Datamuse {
    private val charset = "UTF-8"

    fun definition(word: String, partOfSpeech: List<String>): Definition? {
        val raw = URL("https://api.datamuse.com/words?sp=${URLEncoder.encode(word, charset)}&md=d").readText()
        @Suppress("UNCHECKED_CAST")
        val array = Parser().parse(StringBuilder(raw)) as JsonArray<JsonObject>
        return if (array.isNotEmpty()) {
            val json = array.first()
            val subj = json.string("word")
            val defs = json.array<String>("defs")
            if (defs != null && defs.isNotEmpty()) {
                val filteredDefs = (
                    if (partOfSpeech.isEmpty()) defs
                    else defs.filter { def -> partOfSpeech.any { part -> def.startsWith(part) } }
                )
                if (filteredDefs.isNotEmpty()) {
                    val def = filteredDefs[Math.round(Math.random() * (filteredDefs.size - 1)).toInt()]
                            .split("\t")
                    Definition(subj.orEmpty(), def[0], def[1])
                } else null
            } else null
        } else null
    }
}
