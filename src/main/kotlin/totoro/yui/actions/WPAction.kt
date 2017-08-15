package totoro.yui.actions

import totoro.yui.client.IRCClient


class WPAction : Action {
    private fun estimate(wp: Int) : String {
        return if (wp < 0)
            "lucky man"
        else when (wp) {
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

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "wp", "estimate", "howmuch" -> {
                    val rawNumber = command.words.getOrNull(1)
                    if (rawNumber == null) client.send(command.chan, "i need a number!")
                    else {
                        try {
                            val number = Integer.parseInt(rawNumber)
                            client.send(command.chan, estimate(number))
                        } catch (e: NumberFormatException) {
                            client.send(command.chan, "this is not a number :< gimme a number!")
                        }
                    }
                    return null
                }
            }
        }
        return command
    }
}
