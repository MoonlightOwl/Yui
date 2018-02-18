package totoro.yui.util

@Suppress("MemberVisibilityCanBePrivate")
object LanguageHelper {
    private val cyrillic = 'А' .. 'я'
    private val latinLower = ('a' .. 'z')
    private val latinUpper = ('A' .. 'Z')

    fun isLatin(char: Char) = latinLower.contains(char) || latinUpper.contains(char)
    fun isCyrillic(char: Char) = cyrillic.contains(char) || char == 'ё' || char == 'Ё'

    fun detect(message: String): Language {
        var cyr = 0
        var lat = 0
        message.forEach {
            if (isLatin(it)) lat++
            if (isCyrillic(it)) cyr++
        }
        return if (cyr > lat)
            Language.RUSSIAN
        else
            Language.ENGLISH
    }
}
