package example.com.diary.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Translation")
data class TranslationRoomEntity(
    val langFrom: String,
    val textFrom: String,
    val langTo: String,
    val textTo: String,
    val isFavourite: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0
}