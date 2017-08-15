package totoro.yui.actions

import totoro.yui.client.IRCClient


class TranslitAction: Action {
    private val straight = hashMapOf(
            'a' to 'ф', 'b' to 'и', 'c' to 'с', 'd' to 'в', 'e' to 'у', 'f' to 'а', 'g' to 'п', 'h' to 'р',
            'i' to 'ш', 'j' to 'о', 'k' to 'л', 'l' to 'д', 'm' to 'ь', 'n' to 'т', 'o' to 'щ', 'p' to 'з',
            'q' to 'й', 'r' to 'к', 's' to 'ы', 't' to 'е', 'u' to 'г', 'v' to 'м', 'w' to 'ц', 'x' to 'ч',
            'y' to 'н', 'z' to 'я',
            '`' to 'ё', '[' to 'х', ']' to 'ъ', ';' to 'ж', '\'' to 'э', ',' to 'б', '.' to 'ю',
            '~' to 'Ё', '{' to 'Х', '}' to 'Ъ', ':' to 'Ж', '"' to 'Э', '<' to 'Б', '>' to 'Ю',
            '/' to '.', '?' to ',', '@' to '"', '#' to '№', '$' to ';', '^' to ':', '&' to '?'
    )
    private val reversed = straight.entries.associateBy({ it.value }) { it.key }

    private fun transliterate(phrase: String) : String {
        return if (phrase.isNotEmpty()) {
            // detect which way we need to transliterate this string
            val rules = if (reversed.containsKey(phrase[0]) ||
                    reversed.containsKey(phrase[0].toLowerCase())) reversed else straight
            // convert
            phrase.map {
                when {
                    rules.contains(it) -> rules[it]
                    rules.contains(it.toLowerCase()) -> rules[it.toLowerCase()]?.toUpperCase()
                    else -> it
                }
            }.joinToString("")
        } else phrase
    }

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "tt", "tr", "trans", "translit", "transliterate" -> {
                    val nickname = if (command.words.size > 1) command.words[1] else command.user
                    val lastPhrase = client.history.lastByUser(command.chan, nickname)
                    if (lastPhrase != null) client.send(command.chan, transliterate(lastPhrase.message))
                    else client.send(command.chan, "what do i need to tt?")
                    return null
                }
            }
        }
        return command
    }
}
