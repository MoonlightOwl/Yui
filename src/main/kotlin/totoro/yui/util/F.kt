package totoro.yui.util

/**
 * IRC text formatting codes
 */

@Suppress("unused", "MemberVisibilityCanBePrivate")
object F {
    const val Bold = "\u0002"
    const val Italic = "\u001D"
    const val Underlined = "\u001F"
    const val Reversed = "\u0016"

    const val Default = "\u000399"
    const val White = "\u000300"
    const val Black = "\u000301"
    const val Blue = "\u000302"
    const val Green = "\u000303"
    const val Red = "\u000304"
    const val Brown = "\u000305"
    const val Purple = "\u000306"
    const val Orange = "\u000307"
    const val Yellow = "\u000308"
    const val Lime = "\u000309"
    const val Teal = "\u000310"
    const val Cyan = "\u000311"
    const val Royal = "\u000312"
    const val Pink = "\u000313"
    const val Gray = "\u000314"
    const val Silver = "\u000315"

    const val Reset = "\u000F"

    fun mix(foreground: String, background: String): String {
        return foreground + "," + background.drop(1)
    }
    val colors = arrayOf(
            White, Black, Blue, Green, Red, Brown, Purple, Orange,
            Yellow, Lime, Teal, Cyan, Royal, Pink, Gray, Silver
    )
}
