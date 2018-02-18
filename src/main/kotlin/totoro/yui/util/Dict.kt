package totoro.yui.util

import totoro.yui.Yui

@Suppress("MemberVisibilityCanBePrivate")
class Dict<T>(val variants: List<T>) {
    companion object {
        val Kawaii = Dict.of("kawaii", ":3", "nya", "${F.Red}♥${F.Reset}", "(〜￣▽￣)〜")
        val Hello = Dict.of("o/", "oi", "ahoy", "hey", "hi", "hello", "aloha", "안녕", "おはよう",
                "anyoung", "ohayou", "ohayou gozaimasu", "nyanpasu~", "nyanpasu", "howdy", "wazzup", "shalom")
        val Bye = Dict.of("bye", "goodbye", "farewell", "au revoir", "o/", "ciao", "sayonara", "shalom",
                "bon voyage", "auf Wiedersehen", "aloha", "have a good day", "take care", "bye bye", "later",
                "see you later", "peace")
        val Greets = Kawaii + Hello
        val Nope = Dict.of("nope", "no", "nay", "not", "no way", "of course not", "-", "i don't think so")
        val Yeah = Dict.of("yeah", "aye", "yes", "yep", "+", "definitely", "of course", "sure",
                "sure enough", "surely", "certainly", "naturally", "absolutely", "yea", "yup")
        val Maybe = Dict.of("maybe", "perhaps", "possibly", "it could be", "mayhap", "most likely", "there is a chance",
                "you don't want to know this", "you can say so", "depending on circumstances")
        val NotSure = Dict.of("?", "huh?", "i don't understand", "i don't know", "...", ":<", "ehhhhhh~",
                "gimme something to work with", "what is this?", "wtf?", "desu", "rippu desu")
        val Excited = Dict.of("doki doki shiteru", "\\o/", "yay", "nice")
        val Upset = Dict.of(":<", "...", "v_v", "-_-")
        val Offended = Dict.of("baka") + Upset
        val Thanks = Dict.of("thanks", "thanks", "thank you", "thx", "thanks a lot", "cheers", "arigatou")
        val Refuse = Dict.of("ask totoro", "if only I can", "i will put that on my schedule",
                "gosh, I really wish I could", "I’m focusing on other things right now", "don't think so",
                "life is too short to do things you don't love", "i would love to, but unfortunately... no",
                "alas, such a task is no match for my incompetency", "i shall not", "i think not", "offer declined",
                "I’m trying to see how long I can go without saying yes")
        val AcceptTask = Dict.of("okey", "consider it done", "no problem", "sure", "got it", "right away")
        val Rip = Dict.of("rip", "rippo", "rust in peppers", "✝", "✞", "ＲＩＰ", "rip irc")

        fun <T> of(vararg variants: T): Dict<T> = Dict(variants.asList())
    }

    operator fun plus(new: List<T>) = Dict(variants + new)
    operator fun plus(other: Dict<T>) = Dict(variants + other.variants)

    operator fun invoke() = pick()
    operator fun invoke(n: Int) = pick(n)

    fun pick() = variants[Yui.Random.nextInt(variants.size)]
    fun pick(n: Int) = (1..n).map { pick() }
}
