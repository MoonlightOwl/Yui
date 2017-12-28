package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import totoro.yui.util.F
import totoro.yui.util.api.Google

private val responses = Dict.of("nope", "nothing", "empty", "cannot find this",
        "dead-end", "even google is helpless here")

class SearchAction : SensitivityAction("g", "google", "search") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val result = Google.search(command.content)
        if (result == null) client.send(command.chan, F.Gray + "[${responses()}]" + F.Reset)
        else client.send(command.chan, F.Yellow + result.second + F.Reset + " / ${result.first}")
        return true
    }
}
