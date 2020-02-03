package example.com.diary.data.repositories.history

import example.com.diary.data.persistence.dao.TranslationDao
import example.com.diary.data.persistence.entity.TranslationRoomEntity
import example.com.diary.domain.models.Translation
import example.com.diary.domain.history.HistoryRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val dao: TranslationDao) :
    HistoryRepository {

    override fun getAllTranslates(): Maybe<List<Translation>> {
        return getAllTranslatesAsync()
    }

    private fun getAllTranslatesAsync(): Maybe<List<Translation>> {
        return dao
            .observeResultList()
            .map { it.map { translationRoomEntity ->
                Translation(
                    translationRoomEntity.langFrom,
                    translationRoomEntity.textFrom,
                    translationRoomEntity.langTo,
                    translationRoomEntity.textTo,
                    translationRoomEntity.isFavourite,
                    translationRoomEntity.id
                )
            }
            }
            .firstElement()
    }

    override fun addTranslation(translation: Translation): Completable {
        return Completable.fromAction {
            val dao: TranslationDao = dao
            val newId = dao.insert(toDb(translation))
            translation.id = newId
        }
    }

    override fun deleteTranslation(translation: Translation): Completable {
        return Completable.fromAction {
            val dao: TranslationDao = dao
            dao.deleteTranslationById(translation.id)
        }
    }

    private fun toDb(translation: Translation): TranslationRoomEntity {
        val entity = TranslationRoomEntity(translation.langFrom,
            translation.textFrom, translation.langTo, translation.textTo,
            translation.isFavourite)
        entity.id = translation.id
        return entity
    }
}