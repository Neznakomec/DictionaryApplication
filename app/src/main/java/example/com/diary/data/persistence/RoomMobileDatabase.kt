package example.com.diary.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import example.com.diary.data.persistence.DatabaseFactory.RoomConstants
import example.com.diary.data.persistence.dao.TranslationDao
import example.com.diary.data.persistence.entity.TranslationRoomEntity

@Database(entities = [TranslationRoomEntity::class], version = RoomConstants.MOBILE_DB_VERSION)
abstract class RoomMobileDatabase : RoomDatabase() {
    abstract fun getTranslationDao(): TranslationDao
}