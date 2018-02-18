package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

/**
 * Action processors do almost all the actual work of this bot.
 * They process incoming commands and send corresponding messages in reply.
 * Each processor can consume given command, or transfer further, to the next processor.
 * CommandAction is a processor that works with generated command instances.
 */

interface CommandAction {
    /**
     * This method must decide, whether given command corresponds to the action.
     * If true, it must `consume` the command, and return null.
     * Otherwise, it must return the same command back, so the next action processor in the queue can process it.
     */
    fun process(client: IRCClient, command: Command): Command?
}
