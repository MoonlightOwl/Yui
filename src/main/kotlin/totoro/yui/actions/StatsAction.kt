package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

class StatsAction : SensitivityAction("stats", "stat", "sss", "statistic", "statistics", "analytics") {
    private val white = "https://stats.fomalhaut.me/"
    private val black = "https://sss.centauri.im/"

    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isNotEmpty()) {
            when (command.args.first()) {
                "black", "rip" -> client.send(command.chan, black)
                else -> client.send(command.chan, white)
            }
        } else client.send(command.chan, white)
        return true
    }
}
