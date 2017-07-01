package totoro.yui

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent


object Yui {
    val Version = "0.1.0"

    val nick = Dict.Nick()
    val name = "Yui the Bot"
    val host = "irc.esper.net"
    val chan = "#meowbeast"

    val history = History(5)

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
        println("< $message")
    }

    @Handler
    fun meow(event: ClientConnectedEvent) {
        println("I am connected!")
    }

    @Handler
    fun incoming(event: ChannelMessageEvent) {
        // log to console
        println("> ${event.actor.nick}: ${event.message}")
        // log to history
        history.add(event.message)
        // process possible commands
        if (event.message.startsWith("~")) process(event.actor.nick, event.message.drop(1))
    }

    fun process(nick: String, message: String) {
        val words = message.split(' ', '\t', '\r', '\n').filterNot { it.isNullOrEmpty() }

        if (words.isNotEmpty()) {
            when (words.first()) {
                "call", "phone" -> send(Dict.Phone())
                "fish" -> send("-fish")
                "fork" -> send("---E")
                "g", "google", "search" -> {
                    val result = Google.search(words.drop(1).joinToString(" "))
                    if (result.first == null) send("\u000314[${result.second}]\u000F")
                    else send("\u000308[${result.second}]\u000F (${result.first})")
                }
                "islucky", "lucky" -> {
                    val value = words.getOrNull(1)
                    if (value == null) send("gimme something to estimate")
                    else send(Action.lucky(value))
                }
                "money" -> send("https://youtu.be/vm2RAFv4pwA")
                "moo" -> send("to moo or not to moo, that is the question")
                "pirate" -> send(Action.pirate(words.drop(1)))
                "tt", "tr", "trans", "translit", "transliterate" ->
                    send(Action.transliterate(history.getFromEnd(1)))
                "rules", "wp", "estimate" -> {
                    val rawNumber = words.getOrNull(1)
                    if (rawNumber == null) send("i need a number!")
                    else {
                        try {
                            val number = Integer.parseInt(rawNumber)
                            send(Action.rules(number))
                        } catch (e: NumberFormatException) {
                            send("this is not a number :< gimme a number!")
                        }
                    }
                }
                "shoot", "fire", "kick", "stab", "gaze", "attack", "glare", "stare" -> {
                    val victim = words.getOrNull(1)
                    val nicks = client.getChannel(chan).get().nicknames
                    send(Action.attack(nick, words.first(), if (victim != null) listOf(victim) else nicks))
                }
                else -> send(Dict.NotSure())
            }
        } else send(Dict.NotSure())
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
