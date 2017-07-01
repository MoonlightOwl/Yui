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


    private val literation = hashMapOf<Char, Char>(
            'a' to 'ф', 'b' to 'и', 'c' to 'с', 'd' to 'в', 'e' to 'у', 'f' to 'а', 'g' to 'п', 'h' to 'р',
            'i' to 'ш', 'j' to 'о', 'k' to 'л', 'l' to 'д', 'm' to 'ь', 'n' to 'т', 'o' to 'щ', 'p' to 'з',
            'q' to 'й', 'r' to 'к', 's' to 'ы', 't' to 'е', 'u' to 'г', 'v' to 'м', 'w' to 'ц', 'x' to 'ч',
            'y' to 'н', 'z' to 'я',
            '`' to 'ё', '[' to 'х', ']' to 'ъ', ';' to 'ж', '\'' to 'э', ',' to 'б', '.' to 'ю',
            '~' to 'Ё', '{' to 'Х', '}' to 'Ъ', ':' to 'Ж',  '"' to 'Э', '<' to 'Б', '>' to 'Ю',
            '/' to '.', '?' to ','
    )

    fun transliterate(phrase: String): String {
        return phrase.map {
            if (literation.contains(it)) literation[it]
            else if (literation.contains(it.toLowerCase())) literation[it.toLowerCase()]?.toUpperCase()
            else it
        }.joinToString("")
    }
}
