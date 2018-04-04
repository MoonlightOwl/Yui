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
    private const val Version = "0.5.0"
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
        Config.load("config.properties")

        // connect to database
        val database = Database("yui.db")
        database.connect()


        // register action processors
        val client = IRCClient()
        client.registerMessageAction(TitleAction.instance)
        client.registerMessageAction(HookAction.instance)
        client.registerMessageAction(QuoteAction.instance(database))
        client.registerCommandAction(EmptyCommandAction())
        client.registerCommandAction(SimpleAction(Dict.Hello.variants, Dict.Greets))
        client.registerCommandAction(SimpleAction(Dict.Kawaii.variants, Dict.Kawaii))
        client.registerCommandAction(SimpleAction(Dict.Bye.variants, Dict.Bye))
        client.registerCommandAction(NewsAction())
        client.registerCommandAction(CoinAction())
        client.registerCommandAction(SearchAction())
        client.registerCommandAction(SimpleAction(listOf("v", "version"), Dict.of(Version)))
        client.registerCommandAction(SimpleAction(listOf("cookie", "cake", "banana"),
                Dict.Kawaii + Dict.Excited + Dict.Thanks + Dict.of("oishii", "yummy")))
        client.registerCommandAction(LuaAction())
        client.registerCommandAction(BackronymAction())
        client.registerCommandAction(DefinitionAction())
        client.registerCommandAction(ThesaurusAction())
        client.registerCommandAction(AntonymsAction())
        client.registerCommandAction(StatsAction())
        client.registerCommandAction(TranslitAction())
        client.registerCommandAction(T9Action())
        client.registerCommandAction(HookAction.instance)
        client.registerCommandAction(PlasmaAction())
        client.registerCommandAction(AnimeAction())
        client.registerCommandAction(WikiAction())
        client.registerCommandAction(WordAction())
        client.registerCommandAction(XkcdAction())
        client.registerCommandAction(DiceAction())
        client.registerCommandAction(WeatherAction())
        client.registerCommandAction(TimeAction())
        client.registerCommandAction(PhoneticsAction())
        client.registerCommandAction(UnicodeAction())
        client.registerCommandAction(RipAction())
        client.registerCommandAction(DescribeAction())
        client.registerCommandAction(RhymeAction())
        client.registerCommandAction(SimpleAction(Dict.Yeah.variants, Dict.Nope))
        client.registerCommandAction(SimpleAction(Dict.Nope.variants, Dict.Yeah))
        client.registerCommandAction(SimpleAction(Dict.Offended.variants, Dict.Offended))
        client.registerCommandAction(SimpleAction(listOf("ball", "?", "8", "8?"), (Dict.Yeah + Dict.Nope + Dict.Maybe)))
        client.registerCommandAction(BroteAction())
        client.registerCommandAction(FishAction())
        client.registerCommandAction(NeverGonnaAction())
        client.registerCommandAction(CitationAction())
        client.registerCommandAction(SmileAction())
        client.registerCommandAction(SimpleAction(listOf("anarchy", "rules", "constitution"), Dict.of(
                "https://git.io/vwLXq", "sabotage the system!", "no gods, no masters!", "raise hell!",
                "get ready for anarchy!", "welcome to #cc.ru", "there's no government", "don't forget to " +
                "eat your lunch, and make some trouble", "hierarchy is chaos, anarchy is solidarity",
                "keep calm and be an anarchist", "power to the users!", "to have free minds, we must have " +
                "free tea", "chaos & anarchy", "one direction: insurrection, one solution: revolution",
                "i suppose what I believe in is peaceful anarchy", "if I can't dance to it, it's not " +
                "my revolution", "what is important is to spread confusion, not eliminate it", "in a world " +
                "like this one, only the random makes sense", "anarchism is democracy taken seriously")))
        client.registerCommandAction(MarkovAction(database))
        client.registerCommandAction(QuoteAction.instance(database))
        client.registerCommandAction(WPAction())
        client.registerCommandAction(ChuckAction())
        client.registerCommandAction(SimpleAction(listOf("fork", "pitchfork", "---E"), Dict.of("---E")))
        client.registerCommandAction(PirateAction())
        client.registerCommandAction(RipOrNotAction())
        client.registerCommandAction(CirclifyAction())
        client.registerCommandAction(SimpleAction(listOf("call", "phone"), Dict.of(
                "hang on a moment, I’ll put you through", "beep-beep-beep...", "rip", "☎",
                "sorry, the balance is not enough", "i’m afraid the line is quite bad",
                "i'm busy at the moment, please leave me a message", "ring ring...", "the phone is broken")))
        client.registerCommandAction(SimpleAction(listOf("money", "balance", "uu"), Dict.of("https://youtu.be/vm2RAFv4pwA")))
        client.registerCommandAction(SimpleAction(listOf("!^mooo*$", "cow", "cowpowers", "cowsay", "cowsays"),
                Dict.of("to moo or not to moo, that is the question")))
        client.registerCommandAction(SimpleAction(listOf("disconnect"), Dict.of("disconnect yourself") + Dict.Nope))
        client.registerCommandAction(SimpleAction(listOf("exit", "quit"),
                Dict.of("try /quit", "there's no exit here") + Dict.Nope + Dict.Rip + Dict.Offended))
        client.registerCommandAction(SimpleAction(listOf("nohello"), Dict.of("http://www.nohello.com/")))
        client.registerCommandAction(SimpleAction(listOf("cat", "kote", "!^meoo*w$", "catpowers", "catsay", "catsays"),
                Dict.of("~(=^–^)", ":3", "=’①。①’=", "meow", "meooow", "=^._.^=", "/ᐠ｡ꞈ｡ᐟ\\")))
        client.registerCommandAction(TotoroAction())
        client.registerCommandAction(PjylsAction())
        client.registerCommandAction(SimpleAction(listOf("flip", "table", "drop"),
                Dict.of("(╯°□°）╯︵ ┻━┻", "(ノಠ益ಠ)ノ彡┻━┻", "(╯°□°）╯︵ ┻━┻")))
        client.registerCommandAction(SimpleAction(listOf("calmdown", "cooldown"),
                Dict.of("https://meduza.io/feature/2017/07/03/vse-besit-kak-perestat-besitsya-po-lyubomu-povodu-instruktsiya")))
        client.registerCommandAction(SimpleAction(listOf("powered", "poweredby", "credits"),
                Dict.of("i'm created with the power of Kotlin, Kitteh IRC lib, Debian and also the forest spirit power :3",
                        "i'm a product of some creative meddling with letters and digits")))
        client.registerCommandAction(InstallAction())
        client.registerCommandAction(SimpleAction(listOf("upgrade"), Dict.of("https://www.youtube.com/watch?v=rzLSmY7c9dY")))
        client.registerCommandAction(SimpleAction(listOf("troll", "arch", "trolling"), Dict.of("take this: `pacman -Syu`")))
        client.registerCommandAction(SimpleAction(listOf("compile", "make", "cmake", "gcc", "build", "cargo", "gradle"), Dict.of(
                "irc.cpp:8:28: missing terminating \" character", "moo.cpp: ld returned 1 exit status",
                "error[E2066]: Invalid MOM inheritance", "error[E2502]: Error resolving #import: Rust is too rusted",
                "error[E2497]: No GUID associated with type: 'fish'", "error[E2427]: 'fork' cannot be a template function",
                "error[E2252]: 'catch' expected", "error[E2323]: Illegal number suffix", "error[E2370]: Simple type name expected",
                "rip.cpp:12:1: null pointer assignment", "error[E2014]: Member is ambiguous: 'gentoo' and 'rippo'",
                "irc.rs:4:20: missing a bunch of opening brackets", "error[E0463]: can't find crate for `uu_contrib`")))
        client.registerCommandAction(SimpleAction(listOf("vk", "vkontakte", "group", "public", "wall"),
                Dict.of("https://vk.com/hashccru")))
        client.registerCommandAction(SimpleAction(listOf("logs", "log", "history"),
                Dict.of("https://logs.fomalhaut.me/")))
        client.registerCommandAction(SimpleAction(listOf("quotes"),
                Dict.of("https://quotes.fomalhaut.me/")))
        client.registerCommandAction(SimpleAction(listOf("h", "help"), Dict.Refuse + Dict.of(
                "please, somebody give this man some help!", "sorry, I cannot help",
                "a little help definitely would not hurt")))
        client.registerCommandAction(SimpleAction(listOf("source", "sources", "src", "github"), Dict.of(
                "hey! it's indecent - to ask such things from a girl",
                "hey! it's indecent - to ask such things from a bot",
                "I'd prefer not to answer that", "that's too private",
                "do you even know Kotlin?", "show me your own sources first",
                "let’s talk about something else", "ask totoro",
                "'nothing' is the source of everything, and 'everything' will become nothing one day")))
        client.registerCommandAction(TitleAction.instance)
        client.registerCommandAction(SimpleAction(listOf("birth", "birthday", "bd"), Dict.of("6/6/17 at 12:07 AM UTC+02")))
        client.registerCommandAction(SimpleAction(listOf("napolitan", "naporitan", "napolitan!", "naporitan!"), Dict.of(
                "https://www.youtube.com/watch?v=6lY730l84aI",
                "https://www.youtube.com/watch?v=nJz57eIwRZo",
                "https://www.youtube.com/watch?v=6TFeGkjN6Rc")))
        client.registerCommandAction(SimpleAction(listOf("roll", "rr"),
                Dict.of("miss!", "miss!", "miss!", "miss!", "BANG!", "miss!", "miss!")))
        client.registerCommandAction(LuckyAction())
        client.registerCommandAction(KotlinAction())
        // if the command does not ring any bells, then show uncertainty
        client.registerCommandAction(UnsureAction())

        // let's go!
        client.start {
            client.login(Config.pass)
            client.broadcast(Dict.Greets())
        }
    }
}

fun main(args: Array<String>) {
    Yui.run()
}
