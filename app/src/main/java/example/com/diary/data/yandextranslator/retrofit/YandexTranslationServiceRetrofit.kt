package example.com.diary.data.yandextranslator.retrofit

import com.google.gson.GsonBuilder
import example.com.diary.data.ITranslationService
import example.com.diary.data.yandextranslator.apikey.TranslationApiKey
import example.com.diary.data.yandextranslator.entity.TranslationResponse
import example.com.diary.data.yandextranslator.entity.TranslationRequest
import io.reactivex.Maybe
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class YandexTranslationServiceRetrofit private constructor(private val translationService: TranslationService) : ITranslationService {

    companion object {
        fun create(): YandexTranslationServiceRetrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(TranslationService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return YandexTranslationServiceRetrofit(
                retrofit.create(TranslationService::class.java)
            )
        }
    }

    override fun requestTranslation(request: TranslationRequest): Maybe<TranslationResponse> {
        val langString = "${request.langFrom}-${request.langTo}"
        val apiKey = TranslationApiKey.getTranslateApiKey()

        return translationService
            .getTranslation(apiKey, request.text, langString)
            .map { retro -> return@map TranslationResponse(retro.code, retro.direction, retro.translationResult) }
    }

}