package totoro.yui.util.api

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

object Backronym {
    fun generate(word: String, success: (String) -> Unit, failure: () -> Unit) {
        "http://backronym.org/server/proccessInput.php".httpPost(listOf("word" to word)).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> success(result.value)
            }
        }
    }
}
