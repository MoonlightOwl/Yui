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
    val Version = "0.2.4"
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
        client.registerAction(EmptyAction())
        client.registerAction(SearchAction())
        client.registerAction(TitleAction())
        client.registerAction(RipAction())
        client.registerAction(TranslitAction())
        client.registerAction(PirateAction())
        client.registerAction(SimpleAction(listOf("cookie", "cake"), Dict.Kawaii + Dict.Excited + Dict.Thanks
                + Dict.of("oishii", "yummy")))
        client.registerAction(CorrectAction())
        client.registerAction(ChuckAction())
        client.registerAction(SimpleAction(listOf("ball", "?", "8", "8?"), (Dict.Yeah + Dict.Nope + Dict.Maybe)))
        client.registerAction(SimpleAction(listOf("call", "phone"), Dict.of(
                "hang on a moment, I’ll put you through", "beep-beep-beep...", "rip", "☎",
                "sorry, the balance is not enough", "i’m afraid the line is quite bad",
                "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken")))
        client.registerAction(SimpleAction(listOf("fork", "pitchfork", "---E"), Dict.of("---E")))
        client.registerAction(SimpleAction(listOf("fish", "minusfish", "-fish"), Dict.of("-fish", "-><>")))
        client.registerAction(SimpleAction(listOf("money", "balance", "uu"), Dict.of("https://youtu.be/vm2RAFv4pwA")))
        client.registerAction(SimpleAction(listOf("moo", "cow", "cowpowers"),
                Dict.of("to moo or not to moo, that is the question")))
        client.registerAction(SimpleAction(listOf("exit", "quit", "q"),
                Dict.of("try /quit", "there's no exit here")
                + Dict.Nope + RipAction.RipDict + Dict.Offended))
        client.registerAction(SimpleAction(Dict.Kawaii.variants, Dict.Kawaii))
        client.registerAction(SimpleAction(Dict.Hello.variants, Dict.Greets))
        client.registerAction(LuckyAction())
        client.registerAction(RulesAction())
        client.registerAction(BroteAction())
        // if no messages hit the command, then show uncertainty
        client.registerAction(UnsureAction())

        // let's go!
        client.broadcast(Dict.Greets())
        client.login(config.pass)
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
