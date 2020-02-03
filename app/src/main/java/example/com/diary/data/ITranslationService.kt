package example.com.diary.data

import example.com.diary.data.yandextranslator.entity.TranslationRequest
import example.com.diary.data.yandextranslator.entity.TranslationResponse
import io.reactivex.Maybe

interface ITranslationService {
    fun requestTranslation(request: TranslationRequest): Maybe<TranslationResponse>
}