package example.com.diary.data.persistence

interface IDatabaseProvider {
    fun disconnect()
    fun init()
    fun getRoomMobileDatabase(): RoomMobileDatabase
}

class DatabaseProvider(private val databaseFactory: DatabaseFactory)
    : IDatabaseProvider {
    @Volatile private var roomMobileDatabase: RoomMobileDatabase? = null

    init {
        init()
    }

    override fun init() {
        roomMobileDatabase = databaseFactory.provideRoomMobileDataBase()
    }

    override fun getRoomMobileDatabase(): RoomMobileDatabase {
        roomMobileDatabase ?: synchronized(this) {
            roomMobileDatabase ?: databaseFactory.provideRoomMobileDataBase().also { roomMobileDatabase = it }
        }

        return roomMobileDatabase!!
    }

    override fun disconnect() {
        roomMobileDatabase?.let {
            it.close()
            roomMobileDatabase = null
        }
    }
}