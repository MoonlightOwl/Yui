package totoro.yui.client

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent
import totoro.yui.Config
import totoro.yui.Log
import totoro.yui.actions.Action
import totoro.yui.actions.Command
import totoro.yui.util.Dict


class IRCClient(val config: Config) {
    private val dict = Dict.of("yui`", "yuki`", "yumi`", "ayumi`")

    val nick = dict()
    val realname = "Yui the Bot"

    val history = History(5)
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

    fun isBroteOnline(): Boolean {
        return config.chan.map { client.getChannel(it) }.flatMap { it.get().nicknames }.contains("brote")
    }

    fun broadcast(message: String) {
        config.chan.forEach { client.sendMessage(it, message) }
        Log.outgoing(message)
    }

    fun send(chan: String, message: String) {
        client.sendMessage(chan, message)
        Log.outgoing("[$chan] $message")
    }

    fun process(chan: String, user: String, message: String) {
        val command = Command(chan, user, message.toLowerCase())
        // call registered action processors
        @Suppress("LoopToCallChain")
        for (action in actions) {
            // command can be consumed by one of processors
            // in this case we do not need to try the rest of actions
            if (action.process(this, command) == null) break;
        }
    }


    @Handler
    fun meow(event: ClientConnectedEvent) {
        Log.info("I'm connected!")
    }

    @Handler
    fun incoming(event: ChannelMessageEvent) {
        Log.incoming("[${event.channel.name}] ${event.actor.nick}: ${event.message}")
        // log to history
        history.add(event.channel.name, event.message)
        // detect and process commands
        when {
            event.message.startsWith("~") ->
                process(event.channel.name, event.actor.nick, event.message.drop(1))
            event.message.startsWith(nick) ->
                process(event.channel.name, event.actor.nick, event.message.drop(nick.length))
            event.message.startsWith("$nick:") ->
                process(event.channel.name, event.actor.nick, event.message.drop(nick.length + 1))
            event.message.startsWith("$nick,") ->
                process(event.channel.name, event.actor.nick, event.message.drop(nick.length + 1))
            // special case, when we must show url titles instead of brote
            event.message.startsWith("http") && !isBroteOnline() ->
                process(event.channel.name, event.actor.nick, event.message)
        }
    }

    @Handler
    fun private(event: PrivateMessageEvent) {
        Log.incoming("[PM] ${event.actor.nick}: ${event.message}")
        history.add(event.actor.nick, event.message)
        process(event.actor.nick, event.actor.nick, event.message)
    }
}
