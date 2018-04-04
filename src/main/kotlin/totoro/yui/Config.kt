package totoro.yui

import totoro.yui.util.LogLevel
import java.io.FileInputStream
import java.util.*

object Config {
    private val splitRegex = Regex("\\s*,\\s*")
    private val prop = Properties()

    var admins: List<String> = listOf()
    lateinit var host: String
    lateinit var chan: List<String>
    var pass: String? = null
    var blackCommands: List<String> = listOf()
    var blackUsers: List<String> = listOf()
    var pm: Boolean = true
    var logLevel: LogLevel = LogLevel.DEBUG
    var markovPath: String? = null
    var markovOrder: Int = 1
    var markovAllowNonLetters = true
    var markovAllowShortLines = false

    fun load(filepath: String) {
        try {
            FileInputStream(filepath).use {
                prop.load(it)
                admins = getList("admins", listOf())
                host = getString("host", "irc.esper.net")
                chan = getList("chan", listOf("#meowbeast"))
                pass = getString("pass", "")
                blackCommands = getList("black_commands", listOf())
                blackUsers = getList("black_users", listOf())
                pm = getBoolean("pm", true)
                val rawLogLevel = getString("log_level", "debug").toLowerCase()
                logLevel = when (rawLogLevel) {
                    "d", "debug" -> LogLevel.DEBUG
                    "i", "info" -> LogLevel.INFO
                    "w", "warning", "warn" -> LogLevel.WARNING
                    else -> LogLevel.ERROR
                }
                markovPath = getString("markov_path", "")
                markovOrder = getInt("markov_order", markovOrder)
                markovAllowNonLetters = getBoolean("markov_allow_non_letters", true)
                markovAllowShortLines = getBoolean("markov_allow_short_lines", false)
            }
        } catch (e: Exception) {
            Log.warn("Cannot load properties file, seting default values.")
            host = "irc.esper.net"
            chan = listOf("#meowbeast")
        }
    }

    private fun getString(key: String, default: String): String {
        return prop.getProperty(key) ?: default
    }
    private fun getList(key: String, default: List<String>): List<String> {
        return prop.getProperty(key)?.split(splitRegex) ?: default
    }
    private fun getBoolean(key: String, default: Boolean): Boolean {
        return if (prop.containsKey(key))
            prop.getProperty(key) == "true"
        else
            default
    }
    private fun getInt(key: String, default: Int): Int {
        return try {
            prop.getProperty(key)?.toInt() ?: default
        } catch (e: Exception) {
            default
        }
    }
}
