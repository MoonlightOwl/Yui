package totoro.yui.db

import java.sql.Connection
import java.sql.PreparedStatement


class MarkovTable(connection: Connection) : Table(connection, "markov") {
    fun init() {
        update("create table if not exists $table (chain text, word text);")
        update("create index if not exists ${table}_chains on $table(chain);")
    }

    // fast, batched inserts for initial database generation
    private var preparedStatement: PreparedStatement? = null
    fun prepareBatch() {
        connection.autoCommit = false
        preparedStatement = connection.prepareStatement("insert into $table values(?, ?);")
    }
    fun addBatch(chain: String, word: String) {
        preparedStatement?.setString(1, chain)
        preparedStatement?.setString(2, word)
        preparedStatement?.addBatch()
    }
    fun commitBatch() {
        preparedStatement?.executeBatch()
        connection.commit()
        preparedStatement?.close()
        connection.autoCommit = true
    }

    // slow single insert
    fun add(chain: String, word: String): Long? {
        return insert("insert into $table values('$chain', '$word');").getOrNull(0)
    }
    fun add(occurence: Occurence): Long? = add(occurence.chain, occurence.word)


    fun random(chain: String): Occurence? {
        val raw = query("select rowid, * from $table where chain = '$chain' order by random() limit 1;")
        var occurence: Occurence? = null
        if (raw.isBeforeFirst) {
            raw.next()
            occurence = Occurence(
                    raw.getLong(1),
                    raw.getString("chain"),
                    raw.getString("word")
            )
        }
        raw.close()
        return occurence
    }
}
