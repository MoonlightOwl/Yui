package totoro.yui

import java.io.FileInputStream
import java.util.*

class Config(private val filepath: String) {
    private val splitRegex = Regex("\\s*,\\s*")
    private val prop = Properties()

    var admins: List<String> = listOf()
    lateinit var host: String
    lateinit var chan: List<String>
    var pass: String? = null
    var blackcommands: List<String> = listOf()
    var blackusers: List<String> = listOf()
    var pm: Boolean = true

    fun load() {
        try {
            FileInputStream(filepath).use {
                prop.load(it)
                admins = getList("admins", listOf())
                host = getString("host", "irc.esper.net")
                chan = getList("chan", listOf("#meowbeast"))
                pass = getString("pass", "")
                blackcommands = getList("blackcommands", listOf())
                blackusers = getList("blackusers", listOf())
                pm = getBoolean("pm", true)
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
}
