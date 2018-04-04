package totoro.yui.db

import java.sql.Connection
import java.sql.ResultSet


open class Table(protected val connection: Connection, protected val table: String) {
    private val timeout = 30

    protected fun update(sql: String): ResultSet {
        val statement = connection.createStatement()
        statement.queryTimeout = timeout
        statement.executeUpdate(sql)
        val result = statement.generatedKeys
        statement.closeOnCompletion()
        return result
    }

    protected fun query(sql: String): ResultSet {
        val statement = connection.createStatement()
        statement.queryTimeout = timeout
        statement.closeOnCompletion()
        return statement.executeQuery(sql)
    }

    protected fun insert(sql: String): List<Long> {
        val raw = update(sql)
        val keys: MutableList<Long> = mutableListOf()
        while (raw.next()) {
            keys.add(raw.getLong(1))
        }
        raw.close()
        return keys
    }

    fun truncate() {
        update("delete from $table;").close()
    }
}
