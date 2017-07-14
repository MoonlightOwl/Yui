package totoro.yui.actions

import totoro.yui.client.IRCClient

class TranslitAction: Action {
    private val literation = hashMapOf<Char, Char>(
            'a' to 'ф', 'b' to 'и', 'c' to 'с', 'd' to 'в', 'e' to 'у', 'f' to 'а', 'g' to 'п', 'h' to 'р',
            'i' to 'ш', 'j' to 'о', 'k' to 'л', 'l' to 'д', 'm' to 'ь', 'n' to 'т', 'o' to 'щ', 'p' to 'з',
            'q' to 'й', 'r' to 'к', 's' to 'ы', 't' to 'е', 'u' to 'г', 'v' to 'м', 'w' to 'ц', 'x' to 'ч',
            'y' to 'н', 'z' to 'я',
            '`' to 'ё', '[' to 'х', ']' to 'ъ', ';' to 'ж', '\'' to 'э', ',' to 'б', '.' to 'ю',
            '~' to 'Ё', '{' to 'Х', '}' to 'Ъ', ':' to 'Ж',  '"' to 'Э', '<' to 'Б', '>' to 'Ю',
            '/' to '.', '?' to ','
    )

    private fun transliterate(phrase: String) : String {
        return phrase.map {
            if (literation.contains(it)) literation[it]
            else if (literation.contains(it.toLowerCase())) literation[it.toLowerCase()]?.toUpperCase()
            else it
        }.joinToString("")
    }

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "tt", "tr", "trans", "translit", "transliterate" -> {
                    val lastPhrase = client.history.last(command.chan)
                    if (lastPhrase != null) client.send(command.chan, transliterate(lastPhrase))
                    else client.send(command.chan, "what do i need to tt?")
                    return null
                }
            }
        }
        return command
    }
}
