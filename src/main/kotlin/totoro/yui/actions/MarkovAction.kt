package totoro.yui.actions

import totoro.yui.Config
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.db.Database
import totoro.yui.util.Dict
import totoro.yui.util.Markov


class MarkovAction(private val database: Database) : SensitivityAction("m", "markov", "talk") {
    private val recommendedMinimalPhraseLength = 7
    private val maxNumberOfAttemts = 5

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
            var attempt = 0
            while (true) {
                val chain = phrase.takeLast(Config.markovOrder).joinToString(Markov.chainDelimiter)
                val suggestion = Markov.suggest(database, chain)
                if (suggestion == Markov.ending) {
                    if (attempt >= maxNumberOfAttemts || phrase.size >= recommendedMinimalPhraseLength) break
                    attempt += 1
                }
                else {
                    phrase.add(suggestion)
                    attempt = 0
                }
            }
            client.send(command.chan, phrase.drop(1).joinToString(" "))
        }
        return true
    }
}
