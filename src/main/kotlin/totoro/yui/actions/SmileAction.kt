package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict


class SmileAction : SensitivityAction("smile", "sweet", "sister", "sadistic", "surprise", "service") {
    @Suppress("PrivatePropertyName")
    private val MaxProgressStackSize = 10
    private val progress: LinkedHashMap<String, Int> = linkedMapOf()

    override fun handle(client: IRCClient, command: Command): Boolean {
        val playerPlace = userProgress(command.user)
        val wordPlace = wordNumber(command.name ?: "smile")
        if (wordPlace - playerPlace == 1) {
            client.send(command.chan, sensitivities[wordPlace + 1])
            // update progress, or remove it, if the end was reached
            val newPlace = progress[command.user]?.plus(2) ?: 1
            if (newPlace >= sensitivities.size)
                progress.remove(command.user)
            else
                progress[command.user] = newPlace
        } else {
            client.send(command.chan, Dict.NotSure())
            progress[command.user] = -1
        }
        // trim extra progress records, in order to save memory
        while (progress.size > MaxProgressStackSize) {
            progress.remove(progress.keys.first())
        }
        return true
    }

    private fun userProgress(name: String): Int = progress[name] ?: -1
    private fun wordNumber(word: String): Int = sensitivities.indexOfFirst { it == word }
}
