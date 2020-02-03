package example.com.diary.domain.favourite

import example.com.diary.domain.models.Translation
import example.com.diary.domain.history.HistoryRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class FavouritesInteractorImpl @Inject constructor(private val historyRepository: HistoryRepository) : FavouritesInteractor {
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
}