package example.com.diary.domain.history

import example.com.diary.domain.models.Translation
import io.reactivex.Completable
import io.reactivex.Maybe

interface HistoryRepository {
    fun getAllTranslates(): Maybe<List<Translation>>
    fun addTranslation(translation: Translation): Completable
    fun deleteTranslation(translation: Translation): Completable
}