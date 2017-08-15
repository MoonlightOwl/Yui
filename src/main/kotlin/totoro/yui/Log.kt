package totoro.yui

/**
 * Handmade logger
 */

object Log {
    private fun log(prefix: String, message: String) {
        println("[$prefix]: $message")
    }

    fun info(message: String) = log("INFO", message)
    fun warn(message: String) = log("WARNING", message)
    fun error(message: String) = log("ERROR", message)
    fun incoming(message: String) = log(">", message)
    fun outgoing(message: String) = log("<", message)
}
