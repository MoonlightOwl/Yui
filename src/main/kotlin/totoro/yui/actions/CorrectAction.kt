package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.YandexSpeller

class CorrectAction : Action {
    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when(command.words.first()) {
                "*", "sp", "sc", "spell", "spellcheck", "gramar", "correct", "t9" -> {
                    val phrase = client.history.getFromEnd(command.chan, 1) ?: ""
                    client.send(command.chan, YandexSpeller.correct(phrase))
                    return null
                }
            }
        }
        return command
    }
}
