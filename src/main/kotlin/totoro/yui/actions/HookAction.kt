package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import totoro.yui.util.F
import totoro.yui.util.LimitedHashMap
import java.util.*

/**
 * Allows user to `hook` the target.
 * When the target writes any message to the channel, the user will receive a notification.
 */

class HookAction : SensitivityAction("hook", "unhook"), MessageAction {
    companion object {
        val instance: HookAction by lazy { HookAction() }
    }

    private val sizeLimit = 100
    private val hooks: LimitedHashMap<String, LinkedList<Pair<String, String?>>> = LimitedHashMap(sizeLimit)
    private val confirmations = Dict.AcceptTask + Dict.of("ok, i'll watch him", "i'll notify you")

    private fun add(user: String, target: String, message: String?): Boolean {
        if (!hooks.containsKey(target)) hooks[target] = LinkedList()
        val hunters = hooks[target]
        return if (hunters != null) {
            if (hunters.size > sizeLimit) hunters.drop(1)
            hunters.add(Pair(user, message))
            true
        } else false
    }
    private fun remove(user: String, target: String) {
        if (hooks.containsKey(target)) {
            val hunters = hooks[target]
            hunters?.removeIf { it.first == user }
            if (hunters?.isEmpty() != false) hooks.remove(target)
        }
    }

    override fun process(client: IRCClient, channel: String, user: String, message: String): String? {
        return if (hooks.containsKey(user)) {
            hooks[user]?.forEach {
                client.send(channel, "${it.first}: alarm! $user is here!")
                if (!it.second.isNullOrEmpty()) client.send(channel, "$user: ${it.second}")
                remove(it.first, user)
            }
            null
        } else message
    }

    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isNotEmpty()) {
            if (command.name == "hook") {
                add(command.user, command.args.first(), command.content.dropWhile { !it.isWhitespace() }.trim())
                client.send(command.chan, confirmations())
            } else {
                command.args.forEach {
                    remove(command.user, it)
                }
                client.send(command.chan, Dict.AcceptTask())
            }
        } else {
            client.send(command.chan, "${F.Gray}who do you want me to ${command.name}?${F.Reset}")
        }
        return true
    }
}
