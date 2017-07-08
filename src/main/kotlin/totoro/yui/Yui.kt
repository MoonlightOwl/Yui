package totoro.yui

import totoro.yui.actions.*
import totoro.yui.client.IRCClient
import totoro.yui.util.Dict
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*


/**
 * Entry point of the bot
 */

object Yui {
    // do not forget to change version in build.gradle
    val Version = "0.2.2"
    val Random = Random(System.currentTimeMillis())

    fun run() {
        // turn off SSL certificate checking
        // yeah, yeah, I know, this is bad, very bad =)
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) { }
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) { }
        })

        val trustAllHostnames = HostnameVerifier { _, _ -> true }

        try {
            System.setProperty("jsse.enableSNIExtension", "false")
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCertificates, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames)
        } catch (e: GeneralSecurityException) {
            throw ExceptionInInitializerError(e)
        }


        // load configuration if available
        val config = Config("config.properties")
        config.load()

        // register action processors
        val client = IRCClient(config)
        client.register(EmptyAction())
        client.register(SearchAction())
        client.register(TitleAction())
        client.register(RipAction())
        client.register(TranslitAction())
        client.register(PirateAction())
        client.register(SimpleAction(listOf("cookie", "cake"), Dict.Kawaii + Dict.Excited + Dict.Thanks
                + Dict.of("oishii", "yummy")))
        client.register(LuckyAction())
        client.register(SimpleAction(listOf("ball", "?", "8", "8?"), (Dict.Yeah + Dict.Nope + Dict.Maybe)))
        client.register(SimpleAction(listOf("call", "phone"), Dict.of(
                "hang on a moment, I’ll put you through", "beep-beep-beep...", "rip", "☎",
                "sorry, the balance is not enough", "i’m afraid the line is quite bad",
                "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken")))
        client.register(SimpleAction(listOf("fish", "minusfish", "-fish"), Dict.of("-fish", "-><>")))
        client.register(SimpleAction(listOf("fork", "pitchfork", "---E"), Dict.of("---E")))
        client.register(SimpleAction(listOf("money", "balance", "uu"), Dict.of("https://youtu.be/vm2RAFv4pwA")))
        client.register(SimpleAction(listOf("moo", "cow", "cowpowers"),
                Dict.of("to moo or not to moo, that is the question")))
        client.register(SimpleAction(listOf("exit", "quit", "q"),
                Dict.of("try /quit", "there's no exit here")
                + Dict.Nope + RipAction.RipDict + Dict.Offended))
        client.register(RulesAction())
        client.register(SimpleAction(Dict.Kawaii.variants, Dict.Kawaii))
        client.register(SimpleAction(Dict.Hello.variants, Dict.Greets))
        client.register(BroteAction())
        // if no messages hit the command, then show uncertainty
        client.register(UnsureAction())

        // let's go!
        client.send(Dict.Greets())
        client.login(config.pass)
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
