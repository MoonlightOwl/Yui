package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class PirateAction : Action {
    val dict = Dict.of (
            "arrr!", "yo-ho-ho!", "yo-ho-ho, and a bottle of rum!",
            "new and russian", "ahoy!", "where's my parrot?", "fire in the hole!", "release the kraken!"
    )

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.firstOrNull() == "pirate") {
            // try to piratify nickname
            val name = command.words.getOrNull(1)
            if (name != null) {
                // search for 'r'
                if (name.contains('r', true)) {
                    client.send(name.replace("r", "rrr", true) + "!")
                    return null
                }
            }
            // or just be a pirate
            client.send(dict())
            return null
        } else return command
    }
}
