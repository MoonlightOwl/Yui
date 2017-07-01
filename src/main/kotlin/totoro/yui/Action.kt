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

    fun transliterate(phrase: String) : String {
        return phrase.map {
            if (literation.contains(it)) literation[it]
            else if (literation.contains(it.toLowerCase())) literation[it.toLowerCase()]?.toUpperCase()
            else it
        }.joinToString("")
    }


    fun rules(wp: Int) : String {
        return if (wp < 0)
                "lucky man"
            else when (wp) {
                0 -> "verbal warning"
                in 1..5 -> "kick"
                in 6..7 -> "one hour mute"
                in 8..9 -> "one day mute"
                in 10..11 -> "one day ban"
                in 12..13 -> "two days mute"
                in 14..15 -> "two days ban"
                in 16..17 -> "three days mute"
                in 18..19 -> "three days ban"
                in 20..22 -> "one week mute"
                in 23..25 -> "one week ban"
                in 26..28 -> "two weeks mute"
                in 29..34 -> "two weeks ban"
                in 35..40 -> "one month ban"
                else -> "banned forever"
            }
    }


    private fun summ(value: String): Int {
        return value.fold(0, { summ, ch -> summ + ch.toInt() })
    }

    fun lucky(value: String) : String {
        val half = Math.ceil(value.length / 2.0).toInt()
        return if (summ(value.dropLast(half)) == summ(value.drop(half)))
            Dict.Yeah()
        else
            Dict.Nope()
    }
}
