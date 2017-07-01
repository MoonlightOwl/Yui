package totoro.yui

object Action {
    fun pirate(words: List<String>) : String {
        if (words.isNotEmpty()) {
            val name = words.first()

            // Search for 'r'
            if (name.contains('r', true)) return name.replace("r", "rrr", true) + "!"
        }

        // Just be a pirate
        return Dict.Pirate()
    }
}
