package totoro.yui.util.api

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import totoro.yui.util.LimitedHashMap
import totoro.yui.util.api.data.Story


object HackerNews {
    private const val MAX_HISTORY_SIZE = 200

    private fun stories(filter: String, success: (MutableList<Long>) -> Unit, failure: () -> Unit) {
        "https://hacker-news.firebaseio.com/v0/${filter}stories.json".httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    @Suppress("UNCHECKED_CAST")
                    val array = Parser().parse(StringBuilder(result.value)) as JsonArray<Long>
                    success(array.value)
                }
            }
        }
    }

    private fun story(id: Long, success: (Story) -> Unit, failure: () -> Unit, then: () -> Unit) {
        "https://hacker-news.firebaseio.com/v0/item/$id.json".httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val json = Parser().parse(StringBuilder(result.value)) as JsonObject
                    val score = json.int("score")
                    val title = json.string("title")
                    val url = json.string("url")
                    if (score != null && title != null && url != null) {
                        success(Story(score, title, url))
                        then()
                    } else failure()
                }
            }
        }
    }

    private fun firstUnreadStory(filter: String, history: LimitedHashMap<Long, Boolean>,
                         success: (Story) -> Unit, failure: () -> Unit, then: () -> Unit) {
        stories(filter,
                { list ->
                    val id = list.first { !history.contains(it) }
                    story(id,
                            { story ->
                                history[id] = true
                                success(story)
                            }, failure, then
                    )
                }, failure
        )
    }

    private val topHistory = LimitedHashMap<Long, Boolean>(MAX_HISTORY_SIZE)
    fun topStory(success: (Story) -> Unit, failure: () -> Unit, then: () -> Unit) =
            firstUnreadStory("top", topHistory, success, failure, then)

    private val bestHistory = LimitedHashMap<Long, Boolean>(MAX_HISTORY_SIZE)
    fun bestStory(success: (Story) -> Unit, failure: () -> Unit, then: () -> Unit) =
            firstUnreadStory("best", bestHistory, success, failure, then)

    private val newHistory = LimitedHashMap<Long, Boolean>(MAX_HISTORY_SIZE)
    fun newStory(success: (Story) -> Unit, failure: () -> Unit, then: () -> Unit) =
            firstUnreadStory("new", newHistory, success, failure, then)
}
