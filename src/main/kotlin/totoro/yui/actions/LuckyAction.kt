package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class LuckyAction: Action {
    private fun summ(value: String): Int {
        return value.fold(0, { summ, ch -> summ + ch.toInt() })
    }
    private fun lucky(value: String) : String {
        val half = Math.ceil(value.length / 2.0).toInt()
        return if (summ(value.dropLast(half)) == summ(value.drop(half)))
            Dict.Yeah()
        else
            Dict.Nope()
    }

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "islucky", "lucky" -> {
                    val value = command.words.getOrNull(1)
                    if (value == null) client.send("gimme something to estimate")
                    else client.send(lucky(value))
                    return null
                }
            }
        }
        return command
    }
}
