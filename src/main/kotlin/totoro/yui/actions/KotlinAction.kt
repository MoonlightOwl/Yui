package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import javax.script.ScriptEngineManager

class KotlinAction : SensitivityAction("kotlin") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val code = command.original.drop(command.name?.length ?: 0)
        try {
            val engine = ScriptEngineManager().getEngineByExtension("kts")!!
            val result = engine.eval(code)
            if (result != null)
                result.toString().split("\n").filter { it.isNotEmpty() }.forEach {
                    client.send(command.chan, it)
                }
            else
                client.send(command.chan, "i don't know how to evaluate this :<")
        } catch (e: Exception) {
            e.localizedMessage.split("\n").filter { it.isNotEmpty() }.forEach {
                client.send(command.chan, F.Red + it + F.Reset)
            }
        }

        return true
    }
}
