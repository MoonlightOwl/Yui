package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import totoro.yui.util.Google

class SearchAction: Action {
    val dict = Dict.of("nope", "nothing", "empty", "cannot find this",
                       "dead-end", "even google is helpless here")

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "g", "google", "search" -> {
                    val result = Google.search(command.words.drop(1).joinToString(" "))
                    if (result == null) client.send("\u000314[${dict()}]\u000F")
                    else client.send("\u000308[${result.second}]\u000F (${result.first})")
                    return null
                }
            }
        }
        return command
    }
}
