package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import totoro.yui.util.api.Google

private val responses = Dict.of("nope", "nothing", "empty", "cannot find this",
        "dead-end", "even google is helpless here")

class SearchAction : SensitivityAction("g", "google", "search") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val result = Google.search(command.content)
        if (result == null) client.send(command.chan, "\u000314[${responses()}]\u000F")
        else client.send(command.chan, "\u000308${result.second}\u000F / ${result.first}")
        return true
    }
}
