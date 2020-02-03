package example.com.diary.data.yandextranslator.entity

const val CODE_OK = 200
const val CODE_API_KEY_INVALID = 401
const val CODE_API_KEY_BLOCKED = 402
const val CODE_LIMIT_EXCEEDED = 404
const val CODE_TEXT_LIMIT_EXCEEDED = 413
const val CODE_TEXT_CANT_BEEN_TRANSLATED = 422
const val CODE_TRANSLATION_DIRECTION_NOT_SUPPORTED = 501

data class TranslationResponse(val code: Int,
                               val lang: String?,
                               val text: List<String>?) {
    fun getResponseText(): String {
        return if (text?.isNotEmpty() == true) {
            text.first()
        } else ""
    }

    companion object {

        fun empty(request: TranslationRequest): TranslationResponse {
            return TranslationResponse(200, "${request.langFrom}-${request.langTo}", emptyList())
        }
    }
}
