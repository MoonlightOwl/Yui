package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeAction : SensitivityAction("t", "time", "d", "date") {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    override fun handle(client: IRCClient, command: Command): Boolean {
        client.send(command.chan, LocalDateTime.now().format(formatter))
        return true
    }
}
