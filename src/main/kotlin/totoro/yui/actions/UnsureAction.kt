package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class UnsureAction: Action {
    override fun process(client: IRCClient, command: Command): Command? {
        client.send(command.chan, Dict.NotSure())
        return null
    }
}
