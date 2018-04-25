package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class TotoroAction : SensitivityAction("totoro") {
    private val maxSquadSize = 15
    private val totoro = arrayOf(
            arrayOf(" _//|", "/oo |", "\\mm_|"),
            arrayOf("|\\\\_ ", "| oo\\", "|_mm/")
    )

    override fun handle(client: IRCClient, command: Command): Boolean {
        val quantity = Math.max(1, Math.min(maxSquadSize,
            if (command.args.isEmpty()) 1
            else command.args.first().toIntOrNull() ?: 1
        ))
        client.sendMultiline(command.chan, generate(quantity))
        return true
    }

    private fun generate(quantity: Int): List<String> {
        val squad = arrayOf(StringBuilder(), StringBuilder(), StringBuilder())
        for (i in 1..quantity) {
            totoro[Yui.Random.nextInt(totoro.size)].zip(squad).forEach { (part, builder) -> builder.append("$part ") }
        }
        return squad.map { it.toString() }
    }
}
