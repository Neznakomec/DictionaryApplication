package example.com.diary.data.persistence

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import example.com.diary.BuildConfig

class DatabaseFactory(private val context: Context) {

    object RoomConstants {
        const val MOBILE_DB_NAME = "HistoryDb.db"
        const val MOBILE_DB_VERSION = 2
    }

    @Synchronized fun provideRoomMobileDataBase(): RoomMobileDatabase {
        return provideRoomMobileDataBaseInternal()
    }

    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Translation ADD COLUMN isFavourite INTEGER DEFAULT 0 NOT NULL")
        }
    }
    /*
    TRUNCATE mode instead of WAL to always have information
    in database file without temp files:
    - There is an additional quasi-persistent "-wal" file and "-shm" shared memory file associated with each database, which can make SQLite less appealing for use as an application file-format.
    - There is the extra operation of checkpointing which, though automatic by default, is still something that application developers need to be mindful of.
    */
    private fun provideRoomMobileDataBaseInternal(): RoomMobileDatabase {
        val builder = Room.databaseBuilder(context, RoomMobileDatabase::class.java, RoomConstants.MOBILE_DB_NAME)
            .addMigrations(MIGRATION_1_2)
        if (BuildConfig.DEBUG) {
            builder.setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        }
        return builder.build()
    }
}