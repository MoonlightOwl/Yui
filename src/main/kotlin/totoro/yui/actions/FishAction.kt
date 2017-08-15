package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class FishAction: Action {
    companion object {
        val FishDict = Dict.of("-fish", "><>", "<><", "<>><", "><>>", "---E", "><*>", "<*><")
    }

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "fish", "-fish", "" -> {
                    client.send(
                            command.chan,
                            (0..(Yui.Random.nextInt(7))).joinToString(" ") { FishDict() }
                    )
                    return null
                }
            }
        }
        return command
    }
}
