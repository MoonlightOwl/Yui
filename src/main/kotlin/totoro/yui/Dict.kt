package totoro.yui

import java.util.*

object Dict {
    private val random = Random(System.currentTimeMillis())

    fun get(words: Array<String>): String {
        return words[random.nextInt(words.size)]
    }

    private val nicks = arrayOf("yui", "yuki", "yumi", "ayumi")
    fun Nick() = get(nicks)

    private val greets = arrayOf("kawaii", ":3", "nya", "o/", "oi", "ahoy")
    fun Greets() = get(greets)

    private val pirate = arrayOf("arrr!", "yo-ho-ho!", "yo-ho-ho, and a bottle of rum!",
            "new and russian", "ahoy!", "where's my parrot?", "fire in the hole!", "release the kraken!")
    fun Pirate() = get(pirate)

    private val notsure = arrayOf("?", ":3", "nya", "i'm here", "aye", ":)", "hello there", "huh?",
            "\\o/")
    fun NotSure() = get(notsure)
}
