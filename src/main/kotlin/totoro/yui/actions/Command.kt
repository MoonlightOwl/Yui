package totoro.yui.actions

/**
 * Small utility class containing all useful info about a received command
 */

class Command(val user: String, val command: String) {
    val words = command.split(' ', '\t', '\r', '\n').filterNot { it.isNullOrEmpty() }
}
