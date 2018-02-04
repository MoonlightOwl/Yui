package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Lua

class LuaAction : SensitivityAction("lua", "l", "lua=", "l=", "calc", "c") {
    private val maxOutputLines = 3
    private val errorRegex = "^input:\\d*:\\s.*$".toRegex()

    override fun handle(client: IRCClient, command: Command): Boolean {
        val code = when {
            command.name == "calc" || command.name == "c" -> "=(${command.content})"
            command.name?.endsWith('=') == true -> "=${command.content}"
            else -> command.content
        }
        val result = Lua.run(code)
        if (result == null)
            client.send(command.chan, "${F.Gray}something went wrong${F.Reset}")
        else {
            val lines = result.split('\n')
            lines.take(maxOutputLines).forEach { line ->
                client.send(command.chan, "lua> ${if (line.matches(errorRegex)) F.Red else F.Yellow}$line${F.Reset}")
            }
            if (lines.size > maxOutputLines)
                client.send(command.chan, "${F.Yellow}(and ${lines.size - maxOutputLines} more...)${F.Reset}")
        }

        return true
    }
}
