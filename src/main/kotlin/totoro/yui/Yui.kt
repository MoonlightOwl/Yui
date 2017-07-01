package totoro.yui

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent


object Yui {
    val nick = Dict.Nick()
    val name = "Yui the Bot"
    val host = "irc.esper.net"
    val chan = "#meowbeast"

    val client = Client.builder()
            .nick(nick)
            .user(nick)
            .name(nick)
            .realName(name)
            .serverHost(host)
            .build()

    fun run() {
        client.addChannel(chan)
        send(Dict.Greets())
        client.eventManager.registerEventListener(this)
    }

    fun send(message: String) {
        client.sendMessage(chan, message)
    }

    @Handler
    fun meow(event: ClientConnectedEvent) {
        println("I am connected!")
    }

    @Handler
    fun incoming(event: ChannelMessageEvent) {
        println("> ${event.actor.nick}: ${event.message}")
        if (event.message.startsWith("~")) process(event.message.drop(1))
    }

    fun process(message: String) {
        val words = message.split(' ', '\t', '\r', '\n').filterNot { it.isNullOrEmpty() }

        if (words.isNotEmpty()) {
            when (words.first()) {
                "pirate" -> send(Action.pirate(words.drop(1)))
            }
        } else send(Dict.NotSure())
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
