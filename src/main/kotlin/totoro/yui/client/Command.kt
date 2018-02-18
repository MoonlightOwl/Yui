package totoro.yui.client

/**
 * Small utility class containing all useful info about a received command
 */

class Command(val chan: String, val user: String, val original: String) {
    companion object {
        fun parse(message: String, channel: String, user: String, botName: String): Command? {
            return when {
                message.startsWith("~") ->
                    Command(channel, user, message.drop(1))
                message.startsWith(botName) ->
                    Command(channel, user, message.drop(message.indexOfFirst { it.isWhitespace() } + 1))
                else ->
                    null
            }
        }
    }

    @Suppress("MemberVisibilityCanPrivate")
    private val words = original.split(' ', '\t', '\r', '\n').filterNot { it.isEmpty() }
    val name = words.getOrNull(0)
    val content = original.drop(name?.length ?: 0).trim()
    val args = words.drop(1)
    val valid = name != null
}
