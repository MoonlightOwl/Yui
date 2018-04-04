package totoro.yui.db

import java.sql.Connection


class QuotesTable(connection: Connection) : Table(connection, "quotes") {
    fun init() {
        update("create table if not exists $table (text string);")
    }

    fun add(quote: Quote): Long? {
        // escape single quotes
        val text = quote.text.replace("'", "''")
        // execute select query
        return insert("insert into $table values('$text');").getOrNull(0)
    }

    fun random(): Quote? {
        val raw = query("select rowid, * from $table order by random() limit 1;")
        val quotes: MutableList<Quote> = mutableListOf()
        while (raw.next()) {
            quotes.add(Quote(
                    raw.getLong(1),
                    raw.getString("text")
            ))
        }
        raw.close()
        return quotes.getOrNull(0)
    }
}
