package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.Language
import totoro.yui.util.LanguageHelper


class TranslitAction : SensitivityAction("tt", "tr", "trans", "translit", "transliterate") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val nickname = if (command.args.isNotEmpty()) command.args.first() else command.user
        val lastPhrase = client.history.lastByUser(command.chan, nickname)
        val text = lastPhrase?.let { transliterate(it.message) } ?: "what do i need to tt?"
        client.send(command.chan, text)
        return true
    }

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

    private fun transliterate(phrase: String): String {
        if (phrase.isEmpty()) return phrase
        // detect which way we need to transliterate this string
        val rules = if (LanguageHelper.detect(phrase) == Language.ENGLISH) straight else reversed
        // convert
        return phrase.map {
            when {
                it in rules -> rules[it]!!
                it.toLowerCase() in rules -> rules[it.toLowerCase()]!!.toUpperCase()
                else -> it
            }
        }.joinToString("")
    }
}
