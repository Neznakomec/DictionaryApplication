package example.com.diary.data.yandextranslator.retrofit

import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationService {
    @GET("translate")
    fun getTranslation(
        @Query("key") apiKey: String?,
        @Query("text") text: String?,
        @Query("lang") languagePair: String?
    ): Maybe<RetrofitTranslation>

    companion object {
        const val BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/"
    }
}