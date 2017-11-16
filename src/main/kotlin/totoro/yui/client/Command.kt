package totoro.yui.client

/**
 * Small utility class containing all useful info about a received command
 */

class Command(val chan: String, val user: String, val original: String) {
    @Suppress("MemberVisibilityCanPrivate")
    val words = original.split(' ', '\t', '\r', '\n').filterNot { it.isEmpty() }
    val name = words.getOrNull(0)
    val args = words.drop(1)
    val valid = name != null
}
