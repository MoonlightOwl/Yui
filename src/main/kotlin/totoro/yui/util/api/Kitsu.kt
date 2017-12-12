package totoro.yui.util.api

import com.beust.klaxon.*
import com.github.kittinunf.fuel.httpGet
import totoro.yui.util.api.data.Anime
import java.net.URLEncoder

object Kitsu {
    private val charset = "UTF-8"

    private fun buildUrl(text: String): String {
        return "https://kitsu.io/api/edge/anime" +
                "?filter[text]=${URLEncoder.encode(text, charset)}" +
                "&page[limit]=1" +
                "&fields[anime]=id,titles,synopsis,ageRating,ageRatingGuide,episodeCount,episodeLength,slug,status,nsfw"
    }

    fun search(text: String): Anime? {
        val (_, _, result) = buildUrl(text).httpGet().header(
                "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                "accept-encoding" to "gzip, deflate, br",
                "accept-language" to "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
                "cache-control" to "max-age=0",
                "upgrade-insecure-requests" to "1",
                "user-agent" to "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36",
                "x-compress" to "null"
        ).responseString()
        val json = Parser().parse(StringBuilder(result.component1() ?: "{}")) as JsonObject
        val data = json.array<JsonObject>("data")
        return if (data != null && data.isNotEmpty()) {
            val item = data.first()
            val id = item.string("id")
            val attributes = item.obj("attributes")
            val titles = attributes?.obj("titles")
            val titleEn = titles?.string("en")
            val titleJp = titles?.string("ja_jp")
            val synopsis = attributes?.string("synopsis")
                    ?.replace("\n", " ")
                    ?.replace("\r", "")
            val ageRating = attributes?.string("ageRating")
            val ageRatingGuide = attributes?.string("ageRatingGuide")
            val episodeCount = attributes?.int("episodeCount")
            val episodeLength = attributes?.int("episodeLength")
            val slug = attributes?.string("slug")
            val status = attributes?.string("status")
            val nsfw = attributes?.boolean("nsfw")
            Anime (
                    id ?: "null",
                    (if (titleJp != null && titleEn != null) "$titleEn - $titleJp"
                    else titleJp ?: (titleEn ?: "noname")),
                    synopsis ?: "",
                    (if (ageRating != null && ageRatingGuide != null) "$ageRating: $ageRatingGuide"
                    else "G: All Ages"),
                    episodeCount ?: 0,
                    episodeLength ?: 0,
                    slug ?: "noname",
                    status ?: "finished",
                    nsfw ?: false
            )
        } else null
    }
}
