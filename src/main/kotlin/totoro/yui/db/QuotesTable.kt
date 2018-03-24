package totoro.yui.db

import java.sql.Connection


class QuotesTable(private val connection: Connection) {
    private val table = "quotes"

    fun init() {
        query("create table if not exists $table (text string)")
    }

    fun add(quote: Quote): Long? {
        // escape single quotes
        val text = quote.text.replace("'", "''")
        // execute select query
        return insert("insert into $table values('$text')").getOrNull(0)
    }

    fun random(): Quote? {
        return select("select rowid, * from $table order by random() limit 1").getOrNull(0)
    }

    private fun query(sql: String) {
        val statement = connection.createStatement()
        statement.queryTimeout = 30
        statement.executeUpdate(sql)
        statement.generatedKeys
        statement.close()
    }

    private fun insert(sql: String): List<Long> {
        val statement = connection.createStatement()
        statement.queryTimeout = 30
        statement.executeUpdate(sql)
        val rawKeys = statement.generatedKeys
        val keys: MutableList<Long> = mutableListOf()
        while (rawKeys.next()) {
            keys.add(rawKeys.getLong(1))
        }
        statement.close()
        return keys
    }

    private fun select(sql: String): List<Quote> {
        val statement = connection.createStatement()
        statement.queryTimeout = 30
        val rawQuotes = statement.executeQuery(sql)
        val quotes: MutableList<Quote> = mutableListOf()
        while (rawQuotes.next()) {
            quotes.add(Quote(
                    rawQuotes.getLong(1),
                    rawQuotes.getString("text")
            ))
        }
        statement.close()
        return quotes
    }
}
