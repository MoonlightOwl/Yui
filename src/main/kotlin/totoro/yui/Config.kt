package totoro.yui

import java.io.FileInputStream
import java.util.*

class Config(val filepath: String) {
    val prop = Properties()

    lateinit var host: String
    lateinit var chan: String
    var pass: String? = null

    fun load() {
        try {
            FileInputStream(filepath).use {
                prop.load(it)
                host = prop.getProperty("host")
                chan = prop.getProperty("chan")
                pass = prop.getProperty("pass")
            }
        } catch (e: Exception) {
            Log.warn("Cannot load properties file, seting default values.")
            host = "irc.esper.net"
            chan = "#cc.ru"
        }
    }
}
