package totoro.yui.client

import java.util.*

/**
 * Stores some amount of last chat messages for actions to use them.
 */

class History(val size: Int) {
    val history = LinkedList<String>()

    fun add(message: String) {
        history.addLast(message)
        if (history.size > size) history.removeFirst()
    }

    fun get(index: Int) =  history[index]

    fun getFromEnd(index: Int) = history[history.size - 1 - index]

    fun last() = history.last
}
