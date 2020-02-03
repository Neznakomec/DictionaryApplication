package example.com.diary.domain.history

import example.com.diary.domain.models.Result
import example.com.diary.domain.models.RequestToTranslate
import example.com.diary.domain.models.Translation
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class HistoryInteractorImpl @Inject constructor(private val historyRepository: HistoryRepository, private val translationRepository: TranslationRepository) : HistoryInteractor {
    override fun getAllTranslates(): Maybe<List<Translation>> {
        return historyRepository.getAllTranslates()
    }

    override fun getFavouriteTranslates(): Maybe<List<Translation>> {
        return historyRepository.getAllTranslates()
            .map { listInitial -> listInitial.filter { translation -> translation.isFavourite } }
    }

    override fun addTranslation(translation: Translation): Completable {
        return historyRepository.addTranslation(translation)
    }

    override fun deleteTranslation(translation: Translation): Completable {
        return historyRepository.deleteTranslation(translation)
    }

    override fun translateText(request: RequestToTranslate): Maybe<Result<Translation>> {
        return translationRepository.translateText(request)
    }
}