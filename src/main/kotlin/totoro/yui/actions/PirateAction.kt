package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

private val appeals = Dict.of(
        "arrr!", "yo-ho-ho!", "yo-ho-ho, and a bottle of rum!",
        "new and russian", "ahoy!", "where's my parrot?",
        "fire in the hole!", "release the kraken!"
)

class PirateAction : SensitivityAction("pirate") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        // try to piratify nickname
        val name = command.args.firstOrNull()
        val text = if (name?.contains('r') == true)
        // search for 'r'
            name.replace("r", "rrr") + "!"
        else
        // or just be a pirate
            appeals()
        client.send(command.chan, text)
        return true
    }

}
