package totoro.yui.actions

import totoro.yui.client.IRCClient

/**
 * Action processors do almost all the actual work of this bot.
 * They process incoming commands and send corresponding messages in reply.
 * Each processor can consume given command, or transfer further, to the next processor.
 * MessageAction is a processor that wors with common text messages.
 */

interface MessageAction {
    /**
     * This method must decide, whether this action is `interested` in received message.
     * If true, it must `consume` the message, and return null.
     * Otherwise, it must return the same message back, so the next action processor in the queue can process it.
     */
    fun process(client: IRCClient, channel: String, user: String, message: String): String?
}
