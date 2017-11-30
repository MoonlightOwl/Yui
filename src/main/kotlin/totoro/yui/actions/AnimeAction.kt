package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.api.Kitsu

class AnimeAction : SensitivityAction("a", "anime", "oneme", "animu", "onemu", "kitsu") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val anime = Kitsu.search(command.content)
        if (anime == null) client.send(command.chan, "\u000314no anime today, losers\u000F")
        else {
            client.send(command.chan,
                    if (anime.nsfw) "\u000304[NSFW] " else "" +
                    "\u000308${anime.title}\u000F " +
                    "/ ${anime.episodeCount} episode${if (anime.episodeCount > 1) "s" else ""} ${anime.episodeLength}m " +
                    "/ ${anime.status} " +
                    "/ ${anime.rating}"
            )
            client.send(command.chan,
                    "\u001d(" +
                    (if (anime.synopsis.length > 350) anime.synopsis.take(350) + "..." else anime.synopsis) +
                    ")\u000F"
            )
        }
        return true
    }
}
