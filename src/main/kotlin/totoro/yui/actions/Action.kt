package totoro.yui.actions

import totoro.yui.client.IRCClient

/**
 * Action processors do almost all actual work of the bot.
 * They process incoming commands and send corresponding messages in reply.
 * Each processor can consume given command, or transfer further, to the next processor
 */

interface Action {
    fun process(client: IRCClient, command: Command): Command?
}
