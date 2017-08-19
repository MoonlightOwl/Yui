package totoro.yui.actions

/**
 * Small utility class containing all useful info about a received command
 */

class Command(val chan: String, val user: String, val original: String) {
    val words = original.toLowerCase().split(' ', '\t', '\r', '\n').filterNot { it.isEmpty() }
    val name = words.getOrNull(0)
    val args = words.drop(1)
    val valid = name != null
}
