package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.db.Database
import totoro.yui.db.Quote
import totoro.yui.util.Dict


class QuoteAction(private val database: Database) : Action {
    private val firstWordRegex = Regex("\\s*\\S+\\s+")

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "q", "quote" -> {
                    // if this is new quote
                    if (command.words.size > 2) {
                        val text = command.orginal.replaceFirst(firstWordRegex, "")
                        val index = database.quotes?.add(Quote(0, text))
                        when (index) {
                            null -> client.send(command.chan,
                                    "something went wrong with my storage " + Dict.Upset())
                            else -> client.send(command.chan, "i'll remember this")
                        }
                    }
                    // else return random quote from database
                    else {
                        val quote = database.quotes?.random()
                        when (quote) {
                            null -> client.send(command.chan,
                                    "no quotes today " + Dict.Kawaii())
                            else -> client.send(command.chan,
                                    "[${quote.id}] ${quote.text}")
                        }
                    }
                    return null
                }
            }
        }
        return command
    }
}
