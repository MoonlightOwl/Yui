package totoro.yui

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.client.ClientReceiveCommandEvent
import org.kitteh.irc.client.library.feature.filter.CommandFilter
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent

object Yui {
    val nick = Dict.Nick()
    val name = "Yui the Bot"
    val host = "irc.esper.net"
    val chan = "#cc.ru"

    val client = Client.builder()
            .nick(nick)
            .user(nick)
            .name(nick)
            .realName(name)
            .serverHost(host)
            .build()

    fun run() {
        client.addChannel(chan)
        client.sendMessage(chan, Dict.Greets())
        client.eventManager.registerEventListener(this)
    }

    @Handler
    fun meow(event: ClientConnectedEvent) {
        println("I am connected!")
    }

    @Handler
    fun incoming(event: ChannelMessageEvent) {
        client.sendMessage(chan, event.message)
        print("!")
    }
}

fun main(args: Array<String>) {
    Yui.run()
}