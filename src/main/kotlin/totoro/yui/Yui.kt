package totoro.yui

import totoro.yui.actions.*
import totoro.yui.client.IRCClient
import totoro.yui.db.Database
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
    @Suppress("MemberVisibilityCanPrivate")
    // do not forget to change version in build.gradle
    val Version = "0.3.12"
    val Random = Random(System.currentTimeMillis())

    fun run() {
        // turn off SSL certificate checking
        // yeah, yeah, I know, this is bad, very bad =)
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
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

        // connect to database
        val database = Database("yui.db")
        database.connect()


        // register action processors
        val client = IRCClient(config)
        client.registerAction(EmptyAction())
        client.registerAction(RipAction())
        client.registerAction(RipOrNotAction())
        client.registerAction(SearchAction())
        client.registerAction(SimpleAction(Dict.Kawaii.variants, Dict.Kawaii))
        client.registerAction(SimpleAction(Dict.Hello.variants, Dict.Greets))
        client.registerAction(TranslitAction())
        client.registerAction(SimpleAction(Dict.Yeah.variants, Dict.Nope))
        client.registerAction(SimpleAction(Dict.Nope.variants, Dict.Yeah))
        client.registerAction(SimpleAction(Dict.Offended.variants, Dict.Offended))
        client.registerAction(TitleAction())
        client.registerAction(SimpleAction(listOf("cookie", "cake"),
                Dict.Kawaii + Dict.Excited + Dict.Thanks + Dict.of("oishii", "yummy")))
        client.registerAction(FishAction())
        client.registerAction(T9Action())
        client.registerAction(QuoteAction(database))
        client.registerAction(SimpleAction(listOf("anarchy", "rules", "constitution"),
                Dict.of("https://git.io/vwLXq", "sabotage the system!", "no gods, no masters!", "raise hell!",
                        "get ready for anarchy!", "welcome to #cc.ru", "there's no government", "don't forget to " +
                        "eat your lunch, and make some trouble", "hierarchy is chaos, anarchy is solidarity",
                        "keep calm and be an anarchist", "power to the users!", "to have free minds, we must have " +
                        "free tea", "chaos & anarchy", "one direction: insurrection, one solution: revolution",
                        "i suppose what I believe in is peaceful anarchy", "if I can't dance to it, it's not " +
                        "my revolution", "what is important is to spread confusion, not eliminate it", "in a world " +
                        "like this one, only the random makes sense", "anarchism is democracy taken seriously")))
        client.registerAction(ChuckAction())
        client.registerAction(SimpleAction(listOf("v", "version"), Dict.of(Version)))
        client.registerAction(WPAction())
        client.registerAction(BroteAction())
        client.registerAction(SimpleAction(listOf("fork", "pitchfork", "---E"), Dict.of("---E")))
        client.registerAction(PirateAction())
        client.registerAction(SimpleAction(listOf("ball", "?", "8", "8?"), (Dict.Yeah + Dict.Nope + Dict.Maybe)))
        client.registerAction(SimpleAction(listOf("call", "phone"), Dict.of(
                "hang on a moment, I’ll put you through", "beep-beep-beep...", "rip", "☎",
                "sorry, the balance is not enough", "i’m afraid the line is quite bad",
                "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken")))
        client.registerAction(SimpleAction(listOf("money", "balance", "uu"), Dict.of("https://youtu.be/vm2RAFv4pwA")))
        client.registerAction(SimpleAction(listOf("moo", "cow", "cowpowers", "cowsay", "cowsays"),
                Dict.of("to moo or not to moo, that is the question")))
        client.registerAction(SimpleAction(listOf("exit", "quit"),
                Dict.of("try /quit", "there's no exit here")
                        + Dict.Nope + RipAction.RipDict + Dict.Offended))
        client.registerAction(SimpleAction(listOf("nohello"), Dict.of("http://www.nohello.com/")))
        client.registerAction(SimpleAction(listOf("roll", "rr"),
                Dict.of("miss!", "miss!", "miss!", "miss!", "BANG!", "misfire!", "miss!")))
        client.registerAction(SimpleAction(listOf("calmdown", "cooldown"),
                Dict.of("https://meduza.io/feature/2017/07/03/vse-besit-kak-perestat-besitsya-po-lyubomu-povodu-instruktsiya")))
        client.registerAction(InstallAction())
        client.registerAction(SimpleAction(listOf("troll", "arch", "trolling"), Dict.of("take this: `pacman -Syu`")))
        client.registerAction(SimpleAction(listOf("cat", "kote", "meow", "catpowers", "catsay", "catsays"),
                Dict.of("~(=^–^)", ":3", "=’①。①’=", "meow", "meooow")))
        client.registerAction(SimpleAction(listOf("powered", "poweredby", "credits"),
                Dict.of("i'm created with the power of Kotlin, Kitteh IRC lib, Debian and the forest spirit :3")))
        client.registerAction(SimpleAction(listOf("compile", "make", "cmake", "gcc", "build"),
                Dict.of("irc.cpp:8:28: missing terminating \" character", "moo.cpp: ld returned 1 exit status",
                        "E2066: Invalid MOM inheritance", "E2502: Error resolving #import: Rust is too rusted",
                        "E2497: No GUID associated with type: 'fish'", "E2427: 'fork' cannot be a template function",
                        "E2252: 'catch' expected", "E2323: Illegal number suffix", "E2370: Simple type name expected",
                        "rip.cpp:12:1: null pointer assignment", "E2014: Member is ambiguous: 'gentoo' and 'rippo' ")))
        client.registerAction(SimpleAction(listOf("vk", "vkontakte", "group", "public", "wall"),
                Dict.of("https://vk.com/hashccru")))
        client.registerAction(LuckyAction())
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
