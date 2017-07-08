package totoro.yui.client

import java.util.*
import kotlin.collections.HashMap

/**
 * Stores some amount of last chat messages for actions to use them.
 */

class History(val size: Int) {
    val history = HashMap<String, LinkedList<String>>()

    fun add(chan: String, message: String) {
        if (!history.contains(chan)) history[chan] = LinkedList<String>()
        history[chan]?.addLast(message)
        if (history[chan]?.size ?: 0 > size) history[chan]?.removeFirst()
    }

    fun get(chan: String, index: Int) = history[chan]?.get(index)

    fun getFromEnd(chan: String, index: Int) = history[chan]?.get((history[chan]?.size ?: 0) - 1 - index)

    fun last(chan: String) = history[chan]?.last
}
