package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

/**
 * CommandAction extension that takes care of matching the command name
 */

abstract class SensitivityAction(protected val sensitivities: List<String>) : CommandAction {
    /**
     * Sensitivity pattern can be a simple string (without whitespace characters),
     * or a regex pattern. In the last case the string must begin with `!` sign.
     */
    private val strings = sensitivities.filter { !it.startsWith("!") }
    private val regexes = sensitivities.filter { it.startsWith("!") }.map { it.drop(1).toRegex() }

    constructor(vararg sensitivities: String) : this(sensitivities.toList())

    /**
     * Compares first word of command with given list of sensitivities.
     * In case of any matches - runs `handle` function and 'absorb' the command.
     * Otherwise - returns the command, so the next action handler in queue can try it.
     */
    override fun process(client: IRCClient, command: Command): Command? {
        val matches = strings.contains(command.name) || regexes.any { it.matches(command.name.orEmpty()) }
        val success = matches && handle(client, command)
        return if (success) null else command
    }

    /**
     * This function must be overrided in any child action class.
     * It is responsible for real actions in behalf of the command.
     * @return true if the command was handled successfully, and false otherwise
     */
    abstract fun handle(client: IRCClient, command: Command): Boolean
}
