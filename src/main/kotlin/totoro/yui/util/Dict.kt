package totoro.yui.util

import totoro.yui.Yui

class Dict<T>(val variants: List<T>) {
    companion object {
        val Kawaii = Dict.of("kawaii", ":3", "nya", "\u000304♥\u000F", "(〜￣▽￣)〜", "nyanpasu~")
        val Hello = Dict.of("o/", "oi", "ahoy", "hey", "hi", "hello",
                            "anyoung", "ohayou", "ohayou gozaimasu", "nyanpasu~")
        val Greets = Kawaii + Hello
        val Nope = Dict.of("nope", "no", "nay", "not", "no way", "of course not", "-")
        val Yeah = Dict.of("yeah", "aye", "yes", "yep", "+", "definitely", "of course", "sure",
                           "sure enough", "surely", "certainly", "naturally", "absolutely", "yea", "yup")
        val NotSure = Dict.of("?", "huh?", "i don't understand", "i don't know", "...", ":<",
                              "gimme something to work with", "what is this?", "wtf?")

        fun <T> of(vararg variants: T): Dict<T> {
            return Dict(variants.asList())
        }
    }

    operator fun invoke() = variants[Yui.Random.nextInt(variants.size)]
    operator fun plus(new: List<T>) = Dict(variants + new)
    operator fun plus(other: Dict<T>) = Dict(variants + other.variants)
}