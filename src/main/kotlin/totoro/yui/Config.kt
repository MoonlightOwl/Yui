package totoro.yui

import java.io.FileInputStream
import java.util.*

class Config(val filepath: String) {
    val prop = Properties()

    lateinit var host: String
    lateinit var chan: String

    fun load() {
        try {
            FileInputStream(filepath).use {
                prop.load(it)
                host = prop.getProperty("host")
                chan = prop.getProperty("chan")
            }
        } catch (e: Exception) {
            Log.warn("Cannot load properties file, seting default values.")
            host = "irc.esper.net"
            chan = "#cc.ru"
        }
    }
}
