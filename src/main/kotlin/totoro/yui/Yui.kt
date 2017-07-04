package totoro.yui

import totoro.yui.actions.*
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import java.util.*

/**
 * Entry point of the bot
 */

object Yui {
    // do not forget to change version in build.gradle
    val Version = "0.1.0"
    val Random = Random(System.currentTimeMillis())

    fun run() {
        val config = Config("config.properties")
        config.load()

        val client = IRCClient(config)
        client.register(EmptyAction())
        client.register(SearchAction())
        client.register(TranslitAction())
        client.register(PirateAction())
        client.register(SimpleAction(listOf("call", "phone"), Dict.of(
                "hang on a moment, I’ll put you through", "beep-beep-beep...", "rip", "☎",
                "sorry, the balance is not enough", "i’m afraid the line is quite bad",
                "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken")))
        client.register(SimpleAction(listOf("fish", "minusfish", "-fish"), Dict.of("-fish", "-><>")))
        client.register(SimpleAction(listOf("fork", "pitchfork", "---E"), Dict.of("---E")))
        client.register(SimpleAction(listOf("money", "balance", "uu"), Dict.of("https://youtu.be/vm2RAFv4pwA")))
        client.register(SimpleAction(listOf("moo", "cow", "cowpowers"),
                Dict.of("to moo or not to moo, that is the question")))
        client.register(SimpleAction(listOf("rip", "rippo"), Dict.of("rip", "rippo", "rip rip", "rust in peppers")))
        client.register(LuckyAction())
        client.register(RulesAction())
        client.register(SimpleAction(Dict.Kawaii.variants, Dict.Kawaii))

        client.send(Dict.Greets())
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
