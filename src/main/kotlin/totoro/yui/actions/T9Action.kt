package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.YandexSpeller

class T9Action : SensitivityAction("*", "sp", "sc", "spell", "spellcheck", "gramar", "correct", "t9", "9") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val phrase = client.history.last(command.chan)?.message
        val text = phrase?.let { YandexSpeller.correct(it) } ?: "i do not remember, what i need to correct?"
        client.send(command.chan, text)
        return true
    }
}
