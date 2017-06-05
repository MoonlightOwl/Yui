package totoro.yui

import java.util.*

object Dict {
    private val random = Random(System.currentTimeMillis())

    fun get(words: Array<String>): String {
        return words[random.nextInt(words.size)]
    }

    private val nicks = arrayOf("yui", "yuki", "yumi")
    fun Nick() = get(nicks)

    private val greets = arrayOf("kawaii", ":3", "nya", "o/")
    fun Greets() = get(greets)
}