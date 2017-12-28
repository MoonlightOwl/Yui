package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Kitsu

class AnimeAction : SensitivityAction("a", "anime", "oneme", "animu", "onemu", "kitsu") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val anime = Kitsu.search(command.content)
        if (anime == null) client.send(command.chan, F.Gray + "no anime today, losers" + F.Reset)
        else {
            client.send(command.chan,
                    if (anime.nsfw) F.Red + "[NSFW] " else "" +
                    F.Yellow + "${anime.title} " + F.Reset +
                    "/ ${anime.episodeCount} episode${if (anime.episodeCount != 1) "s" else ""} ${anime.episodeLength}m " +
                    "/ ${anime.status} " +
                    "/ ${anime.rating} " +
                    "/ https://kitsu.io/anime/${anime.slug}"
            )
            client.send(command.chan,
                    F.Italic + "(" +
                    (if (anime.synopsis.length > 350) anime.synopsis.take(350) + "..." else anime.synopsis) +
                    ")" + F.Reset
            )
        }
        return true
    }
}
