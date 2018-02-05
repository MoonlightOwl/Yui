package totoro.yui.util.api

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import totoro.yui.util.api.data.Weather

object Wttr {
    private val escapeCodesRegex = "\u001B\\[[;\\d]*m".toRegex()

    fun get(location: String, success: (Weather) -> Unit, failure: () -> Unit) {
        "http://wttr.in/$location".httpGet().header("User-Agent" to "curl/7.58.0").responseString { _, _, result ->
            when (result) {
                is Result.Failure -> failure()
                is Result.Success -> {
                    val lines = result.value.split('\n').take(7).map { it.replace(escapeCodesRegex, "") }
                    if (lines[0].startsWith("ERROR")) failure()
                    else {
                        val loc = lines[0].split(':')[1].trim()
                        val condition = lines[2].drop(15).trim()
                        val temperature = lines[3].drop(15).trim()
                        val wind = lines[4].drop(15).trim()
                        val visibility = lines[5].drop(15).trim()
                        val precipitation = lines[6].drop(15).trim()
                        success(Weather(loc, condition, temperature, wind, visibility, precipitation))
                    }
                }
            }
        }
    }
}
