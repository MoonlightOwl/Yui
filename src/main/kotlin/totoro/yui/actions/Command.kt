package totoro.yui.actions

/**
 * Small utility class containing all useful info about a received command
 */

class Command(val chan: String, val user: String, val orginal: String) {
    val words = orginal.toLowerCase().split(' ', '\t', '\r', '\n').filterNot { it.isEmpty() }
}
