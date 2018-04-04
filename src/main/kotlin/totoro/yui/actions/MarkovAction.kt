package totoro.yui.actions

import totoro.yui.Config
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.db.Database
import totoro.yui.util.Dict
import totoro.yui.util.F
import totoro.yui.util.Markov


class MarkovAction(private val database: Database) : SensitivityAction("m", "markov", "talk") {
    private val recommendedMinimalPhraseLength = 7
    private val maxNumberOfAttemts = 5
    private val progressReportPause = 60000

    private val waitPlease = Dict.of(
            "be right back...", "one second...", "wait a minute...",
            "hold on...", "just a moment...", "let me see..."
    )

    override fun handle(client: IRCClient, command: Command): Boolean {
        when (command.args.firstOrNull()) {
            "update" -> update(client, command, false)
            "regenerate" -> {
                if (Config.admins.contains(command.user))
                    update(client, command, true)
                else
                    client.send(command.chan, "${F.Gray}sorry, this is too serious business...${F.Reset}")
            }
            else -> {
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
                val message = phrase.drop(1).joinToString(" ")
                if (message.isNotEmpty())
                    client.send(command.chan, message)
                else
                    client.send(command.chan, "${F.Gray}cannot think of anything funny${F.Reset}")
            }
        }
        return true
    }

    private fun update(client: IRCClient, command: Command, regenerate: Boolean) {
        client.send(command.chan, waitPlease())
        var updateTime = 0L
        (if (regenerate) Markov::regenerate else Markov::update)(database, { filename, progress ->
            if (System.currentTimeMillis() - updateTime > progressReportPause) {
                client.send(command.chan, "${(progress * 100).toInt()}% : $filename ...")
                updateTime = System.currentTimeMillis()
            }
        })
        client.send(command.chan, "database updated!")
    }
}
