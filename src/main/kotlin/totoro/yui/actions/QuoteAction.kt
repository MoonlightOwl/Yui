package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.db.Database
import totoro.yui.db.Quote
import totoro.yui.util.Dict
import totoro.yui.util.F
import totoro.yui.util.LimitedHashMap
import totoro.yui.util.LimitedList


class QuoteAction(private val database: Database) : SensitivityAction("q", "quote", "mq", "multiquote"), MessageAction {
    companion object {
        private var action: QuoteAction? = null
        fun instance(database: Database): QuoteAction {
            if (action == null) action = QuoteAction(database)
            return action!!
        }
    }

    private val unfinishedQuotesLimit = 10
    private val quoteLengthLimit = 100
    private val outputLinesLimit = 5

    private val unfinished = LimitedHashMap<String, LimitedList<String>>(unfinishedQuotesLimit)

    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.name == "q" || command.name == "quote") {
            // process one-line quotes and quote requests
            if (command.args.isNotEmpty()) {
                // if this is a new quote - write it to the database
                save(client, command.chan, Quote(0, command.args.joinToString(" ")))
            } else {
                // else - read a random quote from the database
                val quote = database.quotes?.random()
                var text = quote?.let { "${F.Yellow}#${it.id}${F.Reset}: ${it.text}" } ?: "no quotes today " + Dict.Kawaii()
                val numberOfLines = text.count { it == '\n' }
                if (numberOfLines > outputLinesLimit) text = text.split('\n').take(outputLinesLimit).joinToString("\n") + "\n..."
                client.send(command.chan, text)
            }
        } else {
            // process multi-line quotes creation requests
            if (unfinished.containsKey(command.user)) {
                // finish this multiline quote
                save(client, command.chan, Quote(0, unfinished[command.user]!!.joinToString("\n")))
                unfinished.remove(command.user)
            } else {
                // begin a new multiline quote
                unfinished[command.user] = LimitedList(quoteLengthLimit)
                client.send(command.chan, "enter your quote line by line and then finish it with another ~mq command")
            }
        }
        return true
    }

    override fun process(client: IRCClient, channel: String, user: String, message: String): String? {
        // check if there are any unfinished quotes
        return if (unfinished.size > 0 && unfinished.containsKey(user)) {
            // and then add a new line to the quote
            unfinished[user]!!.add(message)
            null
        } else message
    }

    private fun save(client: IRCClient, channel: String, quote: Quote) {
        if (quote.text.isNotBlank()) {
            val index = database.quotes?.add(quote)
            val text = index?.let { "i'll remember this" } ?: "something went wrong with my storage "+Dict.Upset()
            client.send(channel, text)
        } else {
            client.send(channel, "${F.Gray}you really need to spice up this quote${F.Reset}")
        }
    }
}
