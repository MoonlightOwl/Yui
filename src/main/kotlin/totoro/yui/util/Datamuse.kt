package totoro.yui.util

import com.beust.klaxon.*
import java.net.URL
import java.net.URLEncoder

object Datamuse {
    private val charset = "UTF-8"

    fun thesaurus(word: String): String {
        val raw = URL("https://api.datamuse.com/words?sp=${URLEncoder.encode(word, charset)}&md=d").readText()
        @Suppress("UNCHECKED_CAST")
        val array = Parser().parse(StringBuilder(raw)) as JsonArray<JsonObject>
        val json = array.first()
        val subj = json.string("word")
        val defs = json.array<String>("defs")
        return (
            if (defs != null && defs.isNotEmpty()) {
                val def = defs[Math.round(Math.random() * (defs.size - 1)).toInt()]
                "\u000308$subj\u000F: ${def.first()}  \u001D${def.drop(2)}\u000F"
            } else "no definitions found"
        )
    }
}
