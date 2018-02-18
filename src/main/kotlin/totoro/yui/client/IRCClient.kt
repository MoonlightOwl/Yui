package totoro.yui.client

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.channel.UnexpectedChannelLeaveViaKickEvent
import org.kitteh.irc.client.library.event.channel.UnexpectedChannelLeaveViaPartEvent
import org.kitteh.irc.client.library.event.client.ClientConnectionClosedEvent
import org.kitteh.irc.client.library.event.client.ClientConnectionEstablishedEvent
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent
import org.kitteh.irc.client.library.event.client.ClientReceiveMotdEvent
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent
import totoro.yui.Config
import totoro.yui.Log
import totoro.yui.actions.CommandAction
import totoro.yui.actions.MessageAction
import totoro.yui.util.Dict


@Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
class IRCClient(private val config: Config) {
    private val coolNicknames = Dict.of("yui`", "yuki`", "yumi`", "ayumi`")

    private val nick = coolNicknames()
    private val realname = "Yui the Bot"

    val history = History(100)
    private val client = Client.builder()
            .nick(nick)
            .user(nick)
            .name(nick)
            .realName(realname)
            .serverHost(config.host)
            .build()

    private val commandActions = ArrayList<CommandAction>()
    private val messageActions = ArrayList<MessageAction>()

    private var onceConnected: (() -> Unit)? = null

    init {
        config.chan.forEach { client.addChannel(it) }
        client.eventManager.registerEventListener(this)
    }


    fun connect() {
        client.connect()
    }

    fun login(pass: String?) {
        if (pass != null) {
            client.sendMessage("nickserv", "identify $pass")
            Log.info("Logged in.")
        }
    }

    fun broadcast(message: String) {
        config.chan.forEach { client.sendMessage(it, message) }
        Log.outgoing(message)
    }

    fun send(chan: String, message: String) {
        message.split("\n", "\r").filter { it.isNotEmpty() }.map {
            client.sendMessage(chan, it)
            Log.outgoing("[$chan] $it")
        }
    }

    fun start(onceConnected: () -> Unit) {
        connect()
        this.onceConnected = onceConnected
    }


    fun registerCommandAction(action: CommandAction) {
        commandActions.add(action)
    }
    fun registerMessageAction(action: MessageAction) {
        messageActions.add(action)
    }

    fun isBroteOnline(): Boolean =
            config.chan.map { client.getChannel(it) }.flatMap { it.get().nicknames }.any { it == "brote" }


    private fun processMessage(chan: String, user: String, message: String) {
        Log.incoming("[${ if (chan == user) "PM" else chan }] $user: $message")
        if (!tryActionProcessors(chan, user, message))
            history.add(chan, user, message)
    }
    private fun tryActionProcessors(chan: String, user: String, message: String): Boolean {
        val command = Command.parse(message, chan, user, nick)
        if (command != null) {
            // if the command was parsed successfully we will try to process it via command processors
            if (config.blackusers.contains(user))
                send(chan, "$user: totoro says you are baka " + Dict.Offended())
            else {
                // check commands blacklist
                if (command.name in config.blackcommands && user !in config.admins)
                    send(chan, "totoro says - don't use the ~${command.name} command " + Dict.Upset())
                else {
                    // call registered action processors
                    @Suppress("LoopToCallChain")
                    for (action in commandActions) {
                        // command can be consumed by one of processors
                        // in this case we do not need to try the rest of actions
                        if (action.process(this, command) == null) return true
                    }
                }
            }
        } else {
            // if the message cannot be interpreted as a correct command then we will try common message processors
            for (action in messageActions) {
                if (action.process(this, chan, user, message) == null) return true
            }
        }
        return false
    }


    @Handler
    fun motd(event: ClientReceiveMotdEvent) {
        if (onceConnected != null) {
            onceConnected?.invoke()
            onceConnected = null
        }
    }

    @Handler
    fun meow(event: ClientNegotiationCompleteEvent) =
        Log.info("I'm ready to chat (today I'm $nick)!")

    @Handler
    fun ready(event: ClientConnectionEstablishedEvent) =
        Log.info("I'm connected!")

    @Handler
    fun kawaii(event: ClientConnectionClosedEvent) =
        Log.info("I'm disconnected! ${Dict.Upset()}")

    @Handler
    fun baka(event: UnexpectedChannelLeaveViaKickEvent) = {
        Log.info("I was kicked! ${Dict.Offended}")
        event.channel.join()
    }

    @Handler
    fun baka(event: UnexpectedChannelLeaveViaPartEvent) = {
        Log.info("I did leave, but I do not remember why...")
        event.channel.join()
    }

    @Handler
    fun incoming(event: ChannelMessageEvent) {
        processMessage(event.channel.name, event.actor.nick, event.message)
    }

    @Handler
    fun private(event: PrivateMessageEvent) {
        if (config.pm) {
            processMessage(event.actor.nick, event.actor.nick, event.message)
        }
    }
}
