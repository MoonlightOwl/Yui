package totoro.yui.client

import totoro.yui.util.LimitedList

/**
 * Stores some amount of last chat messages for actions to use them.
 */

@Suppress("unused")
class History(private val size: Int) {
    class Record(val user: String, val message: String)

    private val history = HashMap<String, LimitedList<Record>>()

    fun add(chan: String, user: String, message: String) {
        if (!history.contains(chan)) history[chan] = LimitedList(size)
        history[chan]?.addLast(Record(user, message))
    }

    fun get(chan: String, index: Int) = history[chan]?.get(index)

    fun getFromEnd(chan: String, index: Int) = history[chan]?.get((history[chan]?.size ?: 0) - 1 - index)

    fun last(chan: String) = history[chan]?.last

    fun lastByUser(chan: String, user: String) = history[chan]?.lastOrNull { it.user == user }
}
