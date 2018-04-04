package totoro.yui.db

class Occurence(val id: Long, chain: String, word: String) {
    // escape single quotes
    val chain: String = chain.replace("'", "''")
    val word: String = word.replace("'", "''")
}
