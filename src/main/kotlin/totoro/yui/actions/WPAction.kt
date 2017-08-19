package totoro.yui.actions

import totoro.yui.client.IRCClient


class WPAction : SensitivityAction("wp", "estimate", "howmuch") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val text = command.words.getOrNull(1)?.let {
            it.toIntOrNull()?.let {
                estimate(it)
            } ?: "this is not a number :< gimme a number!"
        } ?: "i need a number!"
        client.send(command.chan, text)
        return true
    }

    private fun estimate(wp: Int): String = when (wp) {
        in Int.MIN_VALUE..-1 -> "lucky man"
        0 -> "verbal warning"
        in 1..5 -> "kick"
        in 6..7 -> "one hour mute"
        in 8..9 -> "one day mute"
        in 10..11 -> "one day ban"
        in 12..13 -> "two days mute"
        in 14..15 -> "two days ban"
        in 16..17 -> "three days mute"
        in 18..19 -> "three days ban"
        in 20..22 -> "one week mute"
        in 23..25 -> "one week ban"
        in 26..28 -> "two weeks mute"
        in 29..34 -> "two weeks ban"
        in 35..40 -> "one month ban"
        else -> "banned forever"
    }
}
