package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class UnsureAction: Action {
    override fun process(client: IRCClient, command: Command): Command? {
        client.send(Dict.NotSure())
        return null
    }
}
