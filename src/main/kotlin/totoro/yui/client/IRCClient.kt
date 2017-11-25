package totoro.yui.client

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent
import org.kitteh.irc.client.library.event.client.ClientConnectionClosedEvent
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent
import totoro.yui.Config
import totoro.yui.Log
import totoro.yui.actions.Action
import totoro.yui.util.Dict


@Suppress("unused")
class IRCClient(private val config: Config) {
    private val dict = Dict.of("yui`", "yuki`", "yumi`", "ayumi`")

    private val nick = dict()
    private val realname = "Yui the Bot"

    val history = History(100)
    private val client = Client.builder()
            .nick(nick)
            .user(nick)
            .name(nick)
            .realName(realname)
            .serverHost(config.host)
            .build()

    private val actions = ArrayList<Action>()

    init {
        client.eventManager.registerEventListener(this)
        config.chan.forEach { client.addChannel(it) }
    }

    fun login(pass: String?) {
        if (pass != null) {
            client.sendMessage("nickserv", "identify $pass")
            Log.info("Logged in.")
        }
    }

    fun registerAction(action: Action) {
        actions.add(action)
    }

    fun isBroteOnline(): Boolean =
            config.chan.map { client.getChannel(it) }.flatMap { it.get().nicknames }.contains("brote")

    fun broadcast(message: String) {
        config.chan.forEach { client.sendMessage(it, message) }
        Log.outgoing(message)
    }

    fun send(chan: String, message: String) {
        message.split("\n").filter { it.isNotEmpty() }.map {
            client.sendMessage(chan, message)
            Log.outgoing("[$chan] $message")
        }
    }

    private fun process(chan: String, user: String, message: String): Boolean {
        // check user blacklist
        if (config.blackusers.contains(user))
            send(chan, "$user: totoro says you are baka " + Dict.Offended())
        else {
            val command = Command(chan, user, message)
            // check commands blacklist
            if (command.name in config.blackcommands && user !in config.admins)
                send(chan, "totoro says - don't use the ~${command.name} command " + Dict.Upset())
            else {
                // call registered action processors
                @Suppress("LoopToCallChain")
                for (action in actions) {
                    // command can be consumed by one of processors
                    // in this case we do not need to try the rest of actions
                    if (action.process(this, command) == null) return true
                }
            }
        }
        return false
    }


    @Handler
    fun meow(event: ClientConnectedEvent) = when (event.client.nick) {
        nick -> Log.info("I'm connected (today I'm $nick)!")
        else -> Log.info("[${event.client.nick} has joined]")
    }

    @Handler
    fun kawaii(event: ClientConnectionClosedEvent) = when (event.client.nick) {
        nick -> Log.info("I'm disconnected! ${Dict.Upset()}")
        else -> Log.info("[${event.client.nick} has quit]")
    }

    @Handler
    fun incoming(event: ChannelMessageEvent) {
        Log.incoming("[${event.channel.name}] ${event.actor.nick}: ${event.message}")
        // detect and process commands
        if (!when {
            event.message.startsWith("~") ->
                process(event.channel.name, event.actor.nick, event.message.drop(1))
            event.message.startsWith("$nick:") ->
                process(event.channel.name, event.actor.nick, event.message.drop(nick.length + 1))
            event.message.startsWith("$nick,") ->
                process(event.channel.name, event.actor.nick, event.message.drop(nick.length + 1))
            event.message.startsWith(nick) ->
                process(event.channel.name, event.actor.nick, event.message.drop(nick.length))
        // special case, when we must show url titles instead of brote
            event.message.contains("http") && !isBroteOnline() ->
                process(event.channel.name, event.actor.nick, event.message)
        // if this was not a command - then log to the history
            else -> false
        }) history.add(event.channel.name, event.actor.nick, event.message)
    }

    @Handler
    fun private(event: PrivateMessageEvent) {
        Log.incoming("[PM] ${event.actor.nick}: ${event.message}")
        if (!process(event.actor.nick, event.actor.nick, event.message))
            history.add(event.actor.nick, event.actor.nick, event.message)
    }
}
