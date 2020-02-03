package example.com.diary.data.persistence.dao

import example.com.diary.data.persistence.entity.TranslationRoomEntity
import io.reactivex.Flowable
import java.util.*

class TranslationDaoMock : TranslationDao {
    var storage: MutableList<TranslationRoomEntity> = ArrayList()

    override fun observeResult(): Flowable<TranslationRoomEntity>? {
        return Flowable.fromIterable(storage)
    }

    override fun observeResultList(): Flowable<List<TranslationRoomEntity>> {
        return Flowable.just(storage)
    }

    override fun insert(toDb: TranslationRoomEntity): Long {
        val maxId = if (storage.isNotEmpty()) Collections.max(
            storage
        ) { o1: TranslationRoomEntity, o2: TranslationRoomEntity ->
            java.lang.Long.compare(
                o1.id,
                o2.id
            )
        }.id else 0
        toDb.id = maxId + 1
        storage.add(toDb)
        return toDb.id
    }

    override fun deleteTranslationById(id: Long) {
        var element: TranslationRoomEntity? = null
        for (entity in storage) {
            if (entity.id == id) {
                element = entity
                break
            }
        }
        if (element != null) {
            storage.remove(element)
        }
    }
}