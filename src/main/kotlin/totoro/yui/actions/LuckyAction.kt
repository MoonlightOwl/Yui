package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class LuckyAction : SensitivityAction("islucky", "lucky") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val value = command.args.firstOrNull()
        val text = value?.let { lucky(it) } ?: "gimme something to estimate"
        client.send(command.chan, text)
        return true
    }

    private fun sum(value: String) = value.fold(0) { acc, ch -> acc + ch.toInt() }

    private fun lucky(value: String): String {
        val half = value.length / 2
        val success = sum(value.dropLast(half)) == sum(value.drop(half))
        return if (success) Dict.Yeah() else Dict.Nope()
    }
}
