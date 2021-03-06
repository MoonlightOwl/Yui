package totoro.yui.db

import java.sql.Connection
import java.sql.PreparedStatement


class MarkovTable(connection: Connection) : Table(connection, "markov") {
    fun init() {
        update("create table if not exists $table (chain text, word text, author text);")
        update("create index if not exists ${table}_chains on $table(chain);")
        update("create table if not exists ${table}_config (name text primary key, value text);")
    }

    // fast, batched inserts for initial database generation
    private var preparedStatement: PreparedStatement? = null
    fun prepareBatch() {
        connection.autoCommit = false
        preparedStatement = connection.prepareStatement("insert into $table values(?, ?, ?);")
    }
    fun addBatch(chain: String, word: String, author: String) {
        preparedStatement?.setString(1, chain)
        preparedStatement?.setString(2, word)
        preparedStatement?.setString(3, author)
        preparedStatement?.addBatch()
    }
    fun commitBatch() {
        preparedStatement?.executeBatch()
        connection.commit()
        preparedStatement?.close()
        connection.autoCommit = true
    }

    // slow single insert
    fun add(chain: String, word: String, author: String): Long? {
        return insert("insert into $table values('$chain', '$word', '$author');").getOrNull(0)
    }
    fun add(occurence: Occurence): Long? = add(occurence.chain, occurence.word, occurence.author)


    fun random(chain: String): Occurence? {
        return randomSql("select rowid, * from $table where chain = '$chain' order by random() limit 1;")
    }

    fun random(chain: String, author: String): Occurence? {
        return randomSql("select rowid, * from $table where chain = '$chain' and author = '$author' " +
                "order by random() limit 1;")
    }

    private fun randomSql(sql: String): Occurence? {
        val raw = query(sql)
        var occurence: Occurence? = null
        if (raw.next()) {
            occurence = Occurence(
                    raw.getLong(1),
                    raw.getString("chain"),
                    raw.getString("word"),
                    raw.getString("author")
            )
        }
        raw.close()
        return occurence
    }

    // read field from the config
    fun getFromConfig(key: String): String? {
        val raw = query("select value from ${table}_config where name = '$key';")
        var value: String? = null
        if (raw.next()) {
            value = raw.getString("value")
        }
        raw.close()
        return value
    }

    // write data to the config
    fun setToConfig(key: String, value: String?) {
        insert("insert or replace into ${table}_config values('$key', '$value');")
    }
}
