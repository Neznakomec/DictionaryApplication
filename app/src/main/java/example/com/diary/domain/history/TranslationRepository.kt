package example.com.diary.domain.history

import example.com.diary.domain.models.Result
import example.com.diary.domain.models.RequestToTranslate
import example.com.diary.domain.models.Translation
import io.reactivex.Maybe

interface TranslationRepository {
    fun translateText(request: RequestToTranslate): Maybe<Result<Translation>>
}