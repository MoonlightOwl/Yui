package totoro.yui.actions

import totoro.yui.Config
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.db.Database
import totoro.yui.util.Dict
import totoro.yui.util.Markov


class MarkovAction(private val database: Database) : SensitivityAction("m", "markov", "talk") {
    private val waitPlease = Dict.of(
            "be right back...", "one second...", "wait a minute...",
            "hold on...", "just a moment...", "let me see..."
    )

    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.firstOrNull() == "update") {
            client.send(command.chan, waitPlease())
            Markov.update(database)
            client.send(command.chan, "database updated!")
        } else {
            val phrase = mutableListOf(Markov.beginning)
            while (true) {
                val chain = phrase.takeLast(Config.markovOrder).joinToString(Markov.chainDelimiter)
                val suggestion = Markov.suggest(database, chain)
                if (suggestion == Markov.ending) break;
                else phrase.add(suggestion)
            }
            client.send(command.chan, phrase.drop(1).joinToString(" "))
        }
        return true
    }
}
