package example.com.diary.data.yandextranslator.retrofit

import com.google.gson.annotations.SerializedName
import java.text.MessageFormat
import java.util.*

/*
Собственно перевод.
translationResult оформлен листом, но в нём замечен пока только один элемент.
 */
class RetrofitTranslation {
    @SerializedName("code")
    var code = 0
    @SerializedName("lang")
    var direction: String? = null
    @SerializedName("text")
    var translationResult: List<String> = ArrayList()

    override fun toString(): String {
        return MessageFormat.format(
            "Code: {0}, Direction: {1}, Translation result: {2}",
            code, direction, translationResult.toString()
        )
    }
}