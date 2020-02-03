package example.com.diary.domain.history

import example.com.diary.domain.models.Result
import example.com.diary.domain.models.RequestToTranslate
import example.com.diary.domain.models.Translation
import io.reactivex.Completable
import io.reactivex.Maybe

interface HistoryInteractor {
    fun getAllTranslates(): Maybe<List<Translation>>
    fun getFavouriteTranslates(): Maybe<List<Translation>>
    fun addTranslation(translation: Translation): Completable
    fun deleteTranslation(translation: Translation): Completable
    fun translateText(request: RequestToTranslate): Maybe<Result<Translation>>
}
