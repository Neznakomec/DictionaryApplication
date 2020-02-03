package example.com.diary.data.yandextranslator.entity

data class TranslationRequest(
    val text: String,
    val langFrom: String,
    val langTo: String
)