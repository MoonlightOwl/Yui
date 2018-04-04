package totoro.yui.util

import totoro.yui.Config
import totoro.yui.db.Database
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


@Suppress("unused")
object Markov {
    private val splitPatternAllNonLetters = "[^\\p{L}0-9']+".toRegex()
    private val splitPatternOnlySpaces = "\\s".toRegex()

    const val beginning = "^"
    const val chainDelimiter = " "
    const val ending = "$"

    /**
     * First remove all chains data and the regenerate it anew
     */
    fun regenerate(database: Database, progress: (String, Float) -> Unit): Boolean {
        database.markov?.truncate()
        database.markov?.setToConfig("first_unread", null)
        return update(database, progress)
    }

    /**
     * Read all files from the logs directory and build the probability tables
     */
    fun update(database: Database, progress: (String, Float) -> Unit): Boolean {
        if (!Config.markovPath.isNullOrEmpty()) {
            val logsDir = File(Config.markovPath)
            if (logsDir.exists()) {
                var files = logsDir.listFiles().sorted()
                val firstUnread = database.markov?.getFromConfig("first_unread")
                if (firstUnread != null && firstUnread != "null") files = files.dropWhile { it.name != firstUnread }
                if (files.isNotEmpty()) {
                    // we will leave the last file because it's probably the today's log, and isn't finished yet
                    val theLastOne = files.last()
                    files = files.dropLast(1)
                    files.forEachIndexed { index, file ->
                        val stream = FileInputStream(file)
                        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
                        database.markov?.prepareBatch()
                        reader.useLines { lines -> lines.forEach { line ->
                            if (line.contains('>') && !line.contains("***")) {
                                val words = splitToWords(line)
                                val author = line.substring(line.indexOf('<') + 1, line.indexOf('>'))
                                if (words.isNotEmpty() && (Config.markovAllowShortLines || words.size > 1)) {
                                    sequencesBy(Config.markovOrder, words, { chain, word ->
                                        database.markov?.addBatch(chain.replace("'", "''"), word.replace("'", "''"), author)
                                    })
                                }
                            }
                        }}
                        database.markov?.commitBatch()
                        reader.close()
                        progress(file.name, index.toFloat() / files.size)
                    }
                    database.markov?.setToConfig("first_unread", theLastOne.name)
                }
                return true
            }
        }
        return false
    }
    private fun splitToWords(line: String): List<String> =
            listOf(beginning) +
            line.drop(line.indexOf('>') + 1)
                    .split(if (Config.markovAllowNonLetters) splitPatternOnlySpaces else splitPatternAllNonLetters)
                    .filter { !it.isEmpty() } +
            ending
    private fun sequencesBy(order: Int, words: List<String>, block: (String, String) -> Unit) {
        for (i in (-order + 1) until (words.size - order)) {
            val key = (i..(i + order - 1))
                    .mapNotNull { words.getOrNull(it)?.toLowerCase() }
                    .joinToString(chainDelimiter)
            val value = words[i + order].toLowerCase()
            block(key, value)
        }
    }

    /**
     * Suggest the next word based on given chain
     */
    fun suggest(database: Database, chain: String): String {
        val suggestion = database.markov?.random(chain)
        return suggestion?.word?.replace("''", "'") ?: ending
    }
    fun suggest(database: Database, chain: String, author: String): String {
        val suggestion = database.markov?.random(chain, author)
        return suggestion?.word?.replace("''", "'") ?: ending
    }
}
