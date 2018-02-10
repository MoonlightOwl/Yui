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
    // do not forget to change version in build.gradle
    private const val Version = "0.4.6"
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
        client.registerAction(TitleAction())
        client.registerAction(SimpleAction(Dict.Hello.variants, Dict.Greets))
        client.registerAction(SimpleAction(Dict.Kawaii.variants, Dict.Kawaii))
        client.registerAction(SimpleAction(Dict.Bye.variants, Dict.Bye))
        client.registerAction(NewsAction())
        client.registerAction(CoinAction())
        client.registerAction(SearchAction())
        client.registerAction(SimpleAction(listOf("v", "version"), Dict.of(Version)))
        client.registerAction(SimpleAction(listOf("cookie", "cake"),
                Dict.Kawaii + Dict.Excited + Dict.Thanks + Dict.of("oishii", "yummy")))
        client.registerAction(LuaAction())
        client.registerAction(BackronymAction())
        client.registerAction(DefinitionAction())
        client.registerAction(ThesaurusAction())
        client.registerAction(AntonymsAction())
        client.registerAction(StatsAction())
        client.registerAction(PlasmaAction())
        client.registerAction(AnimeAction())
        client.registerAction(WikiAction())
        client.registerAction(TranslitAction())
        client.registerAction(T9Action())
        client.registerAction(WordAction())
        client.registerAction(XkcdAction())
        client.registerAction(WeatherAction())
        client.registerAction(TimeAction())
        client.registerAction(PhoneticsAction())
        client.registerAction(UnicodeAction())
        client.registerAction(RipAction())
        client.registerAction(DescribeAction())
        client.registerAction(RhymeAction())
        client.registerAction(SimpleAction(Dict.Yeah.variants, Dict.Nope))
        client.registerAction(SimpleAction(Dict.Nope.variants, Dict.Yeah))
        client.registerAction(SimpleAction(Dict.Offended.variants, Dict.Offended))
        client.registerAction(SimpleAction(listOf("ball", "?", "8", "8?"), (Dict.Yeah + Dict.Nope + Dict.Maybe)))
        client.registerAction(BroteAction())
        client.registerAction(FishAction())
        client.registerAction(NeverGonnaAction())
        client.registerAction(CitationAction())
        client.registerAction(SmileAction())
        client.registerAction(SimpleAction(listOf("anarchy", "rules", "constitution"), Dict.of(
                "https://git.io/vwLXq", "sabotage the system!", "no gods, no masters!", "raise hell!",
                "get ready for anarchy!", "welcome to #cc.ru", "there's no government", "don't forget to " +
                "eat your lunch, and make some trouble", "hierarchy is chaos, anarchy is solidarity",
                "keep calm and be an anarchist", "power to the users!", "to have free minds, we must have " +
                "free tea", "chaos & anarchy", "one direction: insurrection, one solution: revolution",
                "i suppose what I believe in is peaceful anarchy", "if I can't dance to it, it's not " +
                "my revolution", "what is important is to spread confusion, not eliminate it", "in a world " +
                "like this one, only the random makes sense", "anarchism is democracy taken seriously")))
        client.registerAction(ChuckAction())
        client.registerAction(SimpleAction(listOf("fork", "pitchfork", "---E"), Dict.of("---E")))
        client.registerAction(WPAction())
        client.registerAction(PirateAction())
        client.registerAction(RipOrNotAction())
        client.registerAction(CirclifyAction())
        client.registerAction(QuoteAction(database))
        client.registerAction(SimpleAction(listOf("call", "phone"), Dict.of(
                "hang on a moment, I’ll put you through", "beep-beep-beep...", "rip", "☎",
                "sorry, the balance is not enough", "i’m afraid the line is quite bad",
                "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken")))
        client.registerAction(SimpleAction(listOf("money", "balance", "uu"), Dict.of("https://youtu.be/vm2RAFv4pwA")))
        client.registerAction(SimpleAction(listOf("!^mooo*$", "cow", "cowpowers", "cowsay", "cowsays"),
                Dict.of("to moo or not to moo, that is the question")))
        client.registerAction(SimpleAction(listOf("exit", "quit"),
                Dict.of("try /quit", "there's no exit here") + Dict.Nope + Dict.Rip + Dict.Offended))
        client.registerAction(SimpleAction(listOf("nohello"), Dict.of("http://www.nohello.com/")))
        client.registerAction(SimpleAction(listOf("roll", "rr"),
                Dict.of("miss!", "miss!", "miss!", "miss!", "BANG!", "miss!", "miss!")))
        client.registerAction(SimpleAction(listOf("cat", "kote", "!^meoo*w$", "catpowers", "catsay", "catsays"),
                Dict.of("~(=^–^)", ":3", "=’①。①’=", "meow", "meooow", "=^._.^=", "/ᐠ｡ꞈ｡ᐟ\\")))
        client.registerAction(TotoroAction())
        client.registerAction(PjylsAction())
        client.registerAction(SimpleAction(listOf("flip", "table"),
                Dict.of("(╯°□°）╯︵ ┻━┻", "(ノಠ益ಠ)ノ彡┻━┻", "(╯°□°）╯︵ ┻━┻")))
        client.registerAction(SimpleAction(listOf("calmdown", "cooldown"),
                Dict.of("https://meduza.io/feature/2017/07/03/vse-besit-kak-perestat-besitsya-po-lyubomu-povodu-instruktsiya")))
        client.registerAction(SimpleAction(listOf("powered", "poweredby", "credits"),
                Dict.of("i'm created with the power of Kotlin, Kitteh IRC lib, Debian and also the forest spirit power :3")))
        client.registerAction(InstallAction())
        client.registerAction(SimpleAction(listOf("upgrade"), Dict.of("https://www.youtube.com/watch?v=rzLSmY7c9dY")))
        client.registerAction(SimpleAction(listOf("troll", "arch", "trolling"), Dict.of("take this: `pacman -Syu`")))
        client.registerAction(SimpleAction(listOf("compile", "make", "cmake", "gcc", "build"), Dict.of(
                "irc.cpp:8:28: missing terminating \" character", "moo.cpp: ld returned 1 exit status",
                "E2066: Invalid MOM inheritance", "E2502: Error resolving #import: Rust is too rusted",
                "E2497: No GUID associated with type: 'fish'", "E2427: 'fork' cannot be a template function",
                "E2252: 'catch' expected", "E2323: Illegal number suffix", "E2370: Simple type name expected",
                "rip.cpp:12:1: null pointer assignment", "E2014: Member is ambiguous: 'gentoo' and 'rippo' ")))
        client.registerAction(SimpleAction(listOf("vk", "vkontakte", "group", "public", "wall", "news"),
                Dict.of("https://vk.com/hashccru")))
        client.registerAction(SimpleAction(listOf("logs", "log", "history"),
                Dict.of("https://logs.fomalhaut.me/")))
        client.registerAction(SimpleAction(listOf("h", "help"), Dict.Refuse + Dict.of(
                "please, somebody, give this man some help!", "sorry, I cannot help",
                "a little help definitely would not hurt")))
        client.registerAction(SimpleAction(listOf("source", "sources", "src"), Dict.of(
                "hey! it's indecent - to ask such things from a girl",
                "hey! it's indecent - to ask such things from a bot",
                "I'd prefer not to answer that", "that's too private",
                "do you even know Kotlin?", "show me your own sources first",
                "let’s talk about something else", "talk to my lawyer")))
        client.registerAction(SimpleAction(listOf("birth", "birthday", "bd"), Dict.of("6/6/17 at 12:07 AM UTC+02")))
        client.registerAction(LuckyAction())
        client.registerAction(KotlinAction())
        // if the command does not ring any bells, then show uncertainty
        client.registerAction(UnsureAction())

        // let's go!
        client.connect()
        client.login(config.pass)
        client.broadcast(Dict.Greets())
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
