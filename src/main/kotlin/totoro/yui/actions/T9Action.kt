package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.YandexSpeller

class T9Action : SensitivityAction("*", "spellcheck", "gramar", "correct", "t9") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val nickname = if (command.args.isNotEmpty()) command.args.first() else command.user
        val phrase = client.history.lastByUser(command.chan, nickname)
        val text = phrase?.let { YandexSpeller.correct(it.message) }
                ?: F.Gray + "i do not remember, what i need to correct?" + F.Reset
        client.send(command.chan, text)
        return true
    }
}
