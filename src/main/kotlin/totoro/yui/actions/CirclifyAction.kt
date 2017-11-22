package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient


class CirclifyAction : SensitivityAction("circlify", "circled", "circle",
        "roundify", "rounded", "round", "o") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val text = command.original.drop(command.name?.length ?: 0).trim()
        client.send(command.chan, round(text))
        return true
    }

    private val rules = hashMapOf(
            '1' to '①', '2' to '②', '3' to '③', '4' to '④', '5' to '⑤', '6' to '⑥',
            '7' to '⑦', '8' to '⑧', '9' to '⑨', '0' to '⓪',
            'a' to 'ⓐ', 'b' to 'ⓑ', 'c' to 'ⓒ', 'd' to 'ⓓ', 'e' to 'ⓔ', 'f' to 'ⓕ', 'g' to 'ⓖ',
            'h' to 'ⓗ', 'i' to 'ⓘ', 'j' to 'ⓙ', 'k' to 'ⓚ', 'l' to 'ⓛ', 'm' to 'ⓜ', 'n' to 'ⓝ',
            'o' to 'ⓞ', 'p' to 'ⓟ', 'q' to 'ⓠ', 'r' to 'ⓡ', 's' to 'ⓢ', 't' to 'ⓣ', 'u' to 'ⓤ',
            'v' to 'ⓥ', 'w' to 'ⓦ', 'x' to 'ⓧ', 'y' to 'ⓨ', 'z' to 'ⓩ'
    )

    private fun round(phrase: String): String {
        if (phrase.isEmpty()) return phrase
        return phrase.map {
            when {
                it in rules -> rules[it]!!
                it.toLowerCase() in rules -> rules[it.toLowerCase()]!!.toUpperCase()
                else -> it
            }
        }.joinToString(" ")
    }
}
