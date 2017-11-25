package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

abstract class SensitivityAction(protected val sensitivities: List<String>) : Action {

    constructor(vararg sensitivities: String) : this(sensitivities.toList())

    /**
     * Compares first word of command with given list of sensitivities.
     * In case of any matches - runs `handle` function.
     *
     * Sensitivity pattern can be a simple string (without whitespace characters),
     * or a regex pattern. In the last case the string must begin with `!` sign.
     */
    override fun process(client: IRCClient, command: Command): Command? {
        val matches = sensitivities.any { pattern ->
            if (pattern.startsWith("!")) pattern.drop(1).toRegex().matches(command.name.orEmpty())
            else command.name == pattern
        }
        val success = matches && handle(client, command)
        return if (success) null else command
    }

    /**
     * This function must be overrided in any child action class.
     * It is responsible for real actions in behalf of the command.
     */
    abstract fun handle(client: IRCClient, command: Command): Boolean
}
