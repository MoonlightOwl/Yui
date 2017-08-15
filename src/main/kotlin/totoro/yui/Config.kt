package totoro.yui

import java.io.FileInputStream
import java.util.*

class Config(private val filepath: String) {
    private val splitRegex = Regex("\\s*,\\s*")
    private val prop = Properties()

    lateinit var host: String
    lateinit var chan: List<String>
    var pass: String? = null
    var blackcommands: List<String> = listOf()
    var blackusers: List<String> = listOf()

    fun load() {
        try {
            FileInputStream(filepath).use {
                prop.load(it)
                host = getString("host", "irc.esper.net")
                chan = getList("chan", listOf("#meowbeast"))
                pass = getString("pass", "")
                blackcommands = getList("blackcommands", listOf())
                blackusers = getList("blackusers", listOf())
            }
        } catch (e: Exception) {
            Log.warn("Cannot load properties file, seting default values.")
            host = "irc.esper.net"
            chan = listOf("#meowbeast")
        }
    }

    private fun getString(key: String, default: String): String {
        return if (prop.containsKey(key))
            prop.getProperty(key)
        else
            default
    }
    private fun getList(key: String, default: List<String>): List<String> {
        return if (prop.containsKey(key))
            prop.getProperty(key).split(splitRegex)
        else
            default
    }
}
