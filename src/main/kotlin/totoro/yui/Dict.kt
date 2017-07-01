package totoro.yui

import java.util.*

object Dict {
    private val random = Random(System.currentTimeMillis())

    fun get(words: Array<String>): String {
        return words[random.nextInt(words.size)]
    }

    private val nicks = arrayOf("yui`", "yuki`", "yumi`", "ayumi`")
    fun Nick() = get(nicks)

    private val greets = arrayOf("kawaii", ":3", "nya", "o/", "oi", "ahoy")
    fun Greets() = get(greets)

    private val nope = arrayOf("nope", "no", "nay", "not", "no way", "of course not", "-")
    fun Nope() = get(nope)

    private val yeah = arrayOf("yeah", "aye", "yes", "yep", "+", "definitely", "of course", "sure",
            "sure enough", "surely", "certainly", "naturally", "absolutely", "yea", "yup")
    fun Yeah() = get(yeah)

    private val pirate = arrayOf("arrr!", "yo-ho-ho!", "yo-ho-ho, and a bottle of rum!",
            "new and russian", "ahoy!", "where's my parrot?", "fire in the hole!", "release the kraken!")
    fun Pirate() = get(pirate)

    private val notsure = arrayOf("?", ":3", "nya", "i'm here", "aye", ":)", "hello there", "huh?",
            "\\o/", "\u000304♥\u000F", "(〜￣▽￣)〜")
    fun NotSure() = get(notsure)

    private val notfound = arrayOf("nope", "nothing", "empty", "cannot find this",
            "dead-end", "even google is helpless here")
    fun NotFound() = get(notfound)

    private val phone = arrayOf("hang on a moment, I’ll put you through", "beep-beep-beep...",
            "sorry, the balance is not enough", "i’m afraid the line is quite bad",
            "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken", "rip", "☎")
    fun Phone() = get(phone)
}
