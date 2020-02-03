package example.com.diary.domain.models

data class RequestToTranslate(
    val text: String,
    val langFrom: String,
    val langTo: String
)