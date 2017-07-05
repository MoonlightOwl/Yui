package totoro.yui.client

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent
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
        client.addChannel(config.chan)
    }


    fun register(action: Action) {
        actions.add(action)
    }

    fun login(pass: String?) {
        if (pass != null) {
            client.sendMessage("nickserv", "identify $pass")
            Log.info("Logged in.")
        }
    }

    fun send(message: String) {
        client.sendMessage(config.chan, message)
        Log.outgoing(message)
    }

    fun process(user: String, rawCommand: String) {
        val command = Command(user, rawCommand)
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
        Log.incoming("${event.actor.nick}: ${event.message}")
        // log to history
        history.add(event.message)
        // detect and process commands
        when {
            event.message.startsWith("~") -> process(event.actor.nick, event.message.drop(1))
            event.message.startsWith(nick) -> process(event.actor.nick, event.message.drop(nick.length))
            event.message.startsWith("$nick:") -> process(event.actor.nick, event.message.drop(nick.length + 1))
            event.message.startsWith("$nick,") -> process(event.actor.nick, event.message.drop(nick.length + 1))
        }
    }
}
