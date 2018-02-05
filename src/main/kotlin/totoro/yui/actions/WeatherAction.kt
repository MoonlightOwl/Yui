package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Wttr

class WeatherAction : SensitivityAction("weather", "wttr", "w") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        Wttr.get(command.content, { weather ->
            client.send(command.chan, "${weather.location}: ${F.Yellow}${weather.condition} / ${weather.temperature} / " +
                    "${weather.wind} / ${weather.visibility} / ${weather.precipitation}${F.Reset}")
        }, {
            client.send(command.chan, "${F.Gray}the weather is unclear${F.Reset}")
        })
        return true
    }
}
