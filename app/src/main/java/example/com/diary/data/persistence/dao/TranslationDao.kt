package example.com.diary.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import example.com.diary.data.persistence.entity.TranslationRoomEntity
import io.reactivex.Flowable

@Dao
interface TranslationDao {
    @Query("SELECT * FROM Translation ORDER BY Id DESC")
    fun observeResult(): Flowable<TranslationRoomEntity>?

    @Query("SELECT * FROM Translation ORDER BY Id DESC")
    fun observeResultList(): Flowable<List<TranslationRoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(toDb: TranslationRoomEntity): Long

    @Query("DELETE FROM Translation WHERE Id = :id")
    fun deleteTranslationById(id: Long)
}