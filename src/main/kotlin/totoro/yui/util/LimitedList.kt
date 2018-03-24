package totoro.yui.util

import java.util.*

class LimitedList<T>(private val limit: Int): LinkedList<T>() {
    override fun add(element: T): Boolean {
        return if (super.add(element)) { trim(); true } else false
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        trim()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return if (super.addAll(elements)) { trim(); true } else false
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return if (super.addAll(index, elements)) { trim(); true } else false
    }

    override fun addFirst(e: T) {
        super.addFirst(e)
        trim()
    }

    override fun addLast(e: T) {
        super.addLast(e)
        trim()
    }

    private fun trim() {
        if (size > limit) removeRange(0, size - limit)
    }
}
