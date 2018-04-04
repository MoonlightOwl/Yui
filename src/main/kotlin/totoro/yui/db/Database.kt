package totoro.yui.db

import org.sqlite.JDBC
import totoro.yui.Log
import java.sql.Connection
import java.sql.DriverManager

@Suppress("unused")
class Database(private val filename: String) {
    private var connection: Connection? = null

    var quotes: QuotesTable? = null
    var markov: MarkovTable? = null

    fun connect() {
        try {
            DriverManager.registerDriver(JDBC())
            connection = DriverManager.getConnection("jdbc:sqlite:$filename")
            initTables(connection)
            Log.info("Succesfully connected to the database: $filename")
        } catch (e: Exception) {
            Log.error("Cannot establish connection to the database!")
            Log.debug(e)
        }
    }

    private fun initTables(connection: Connection?) {
        if (connection != null) {
            quotes = QuotesTable(connection)
            quotes?.init()
            markov = MarkovTable(connection)
            markov?.init()
        } else {
            Log.error("WTF? Database connection appears to be broken...")
        }
    }

    fun close() {
        connection?.close()
    }
}
