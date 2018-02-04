package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Lua

class LuaAction : SensitivityAction("lua", "l", "lua=", "l=") {
    private val errorRegex = "^input:\\d*:\\s.*$".toRegex()

    override fun handle(client: IRCClient, command: Command): Boolean {
        val code = when {
            command.name?.endsWith('=') == true -> "=${command.content}"
            else -> command.content
        }
        val result = Lua.run(code)
        if (result == null)
            client.send(command.chan, "${F.Gray}something went wrong${F.Reset}")
        else
            client.send(command.chan, "${if (result.matches(errorRegex)) F.Red else F.Yellow}$result${F.Reset}")
        return true
    }
}
