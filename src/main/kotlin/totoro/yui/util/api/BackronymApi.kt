package totoro.yui.util.api

interface BackronymApi {
    fun generate(word: String, success: (String) -> Unit, failure: () -> Unit)
}
