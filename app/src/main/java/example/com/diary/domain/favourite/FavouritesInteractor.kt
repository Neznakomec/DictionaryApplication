package example.com.diary.domain.favourite

import example.com.diary.domain.models.Translation
import io.reactivex.Completable
import io.reactivex.Maybe

interface FavouritesInteractor {
    fun getAllTranslates(): Maybe<List<Translation>>
    fun getFavouriteTranslates(): Maybe<List<Translation>>
    fun addTranslation(translation: Translation): Completable
    fun deleteTranslation(translation: Translation): Completable
}
