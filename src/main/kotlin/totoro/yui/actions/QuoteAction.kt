package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.db.Database
import totoro.yui.db.Quote
import totoro.yui.util.Dict


class QuoteAction(private val database: Database) : SensitivityAction("q", "quote") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isNotEmpty()) {
            // if this is new quote
            val quote = Quote(0, command.args.joinToString(" "))
            val index = database.quotes?.add(quote)
            val text = index?.let { "i'll remember this" } ?: "something went wrong with my storage " + Dict.Upset()
            client.send(command.chan, text)
        } else {
            // else return random quote from database
            val quote = database.quotes?.random()
            val text = quote?.let { "[${it.id}] ${it.text}" } ?: "no quotes today " + Dict.Kawaii()
            client.send(command.chan, text)
        }
        return true
    }
}
