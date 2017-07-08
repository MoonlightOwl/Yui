package totoro.yui.actions

import totoro.yui.client.IRCClient

class BroteAction: Action {
    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "brote" -> {
                    client.send(
                            command.chan,
                            if (client.isBroteOnline()) "\u000303[online]\u000F" else "\u000304[broken]\u000F"
                    )
                    return null
                }
            }
        }
        return command
    }
}
