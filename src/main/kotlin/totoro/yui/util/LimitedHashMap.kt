package totoro.yui.util

class LimitedHashMap<A, B>(private val limit: Int) : LinkedHashMap<A, B>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<A, B>?): Boolean {
        return size > limit
    }
}
