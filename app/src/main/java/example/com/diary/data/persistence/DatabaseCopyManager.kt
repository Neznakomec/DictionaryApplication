package example.com.diary.data.persistence

import android.content.Context
import android.os.Environment
import example.com.diary.data.persistence.DatabaseFactory.RoomConstants
import example.com.diary.data.fileutils.FileUtils
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class DatabaseCopyManager @Inject constructor(
    private val context: Context,
    private val databaseProvider: IDatabaseProvider
) {
    fun copyToRoot(): Completable {
        return Completable.fromAction {
            databaseProvider.disconnect()
            val sdcardDir = Environment.getExternalStorageDirectory()
            val mobileDbName = File(sdcardDir, RoomConstants.MOBILE_DB_NAME)
            val mobileDb = mobileDbName.absolutePath
            val mobilePath =
                context.getDatabasePath(RoomConstants.MOBILE_DB_NAME).path
            FileUtils.copyFile(mobilePath, mobileDb)
            databaseProvider.init()
        }
    }
}