package example.com.diary.data.repositories.history

import example.com.diary.data.persistence.dao.TranslationDao
import example.com.diary.data.persistence.dao.TranslationDaoMock
import example.com.diary.data.persistence.entity.TranslationRoomEntity
import example.com.diary.domain.models.Translation
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class HistoryRepositoryImplTest {

    private lateinit var translationDao: TranslationDao
    private lateinit var historyRepository: HistoryRepositoryImpl

    private val entity = TranslationRoomEntity(
        langFrom = "en", langTo = "ru", isFavourite = true,
        textFrom = "apple", textTo = "яблоко"
    )

    private val resultList = listOf(
        Translation(
            langFrom = "en", langTo = "ru", isFavourite = true,
            textFrom = "apple", textTo = "яблоко", id = 1
        )
    )

    @Before
    fun setUp() {
        translationDao = TranslationDaoMock()
        historyRepository = HistoryRepositoryImpl(translationDao)
    }

    @Test
    fun `test that getAllTranslates returns full list`() {
        // when
        addItem()
        addItem()

        val testObserver: TestObserver<List<Translation>> = TestObserver.create()
        // action
        historyRepository.getAllTranslates().subscribe(testObserver)
        // assert & verify
        testObserver.assertValue { list ->
            val translation: Translation = list.first()

            return@assertValue translation.isFavourite == entity.isFavourite
                    && translation.textFrom == entity.textFrom
                    && translation.textTo == entity.textTo
        }
    }

    @Test
    fun `add item and check it in repository`() {
        // when
        addItem()
        val testObserver: TestObserver<List<Translation>> = TestObserver.create()
        // action
        historyRepository.getAllTranslates().subscribe(testObserver)
        // assert & verify
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertResult(resultList)
    }

    @Test
    fun `delete added item from repository`() {
        // when
        addItem()
        // action
        translationDao.deleteTranslationById(1)
        val testObserver: TestObserver<List<Translation>> = TestObserver.create()
        historyRepository.getAllTranslates().subscribe(testObserver)
        // assert & verify
        testObserver.assertValue { list -> list.isEmpty() }
    }

    private fun addItem() {
        translationDao.insert(entity)
    }

    @Test
    fun `mark item as not favourite`() {
        // when
        val testObserver: TestObserver<List<Translation>> = TestObserver.create()
        // action
        val translation = resultList.first()
        translation.isFavourite = false
        historyRepository.addTranslation(translation).subscribe()
        historyRepository.getAllTranslates().subscribe(testObserver)
        // assert & verify
        testObserver.assertResult(resultList)
    }

    @Test
    fun `mark item as favourite`() {
        // when
        val testObserver: TestObserver<List<Translation>> = TestObserver.create()
        // action
        val translation = resultList.first()
        historyRepository.deleteTranslation(translation).subscribe()
        translation.isFavourite = true
        historyRepository.addTranslation(translation).subscribe()
        historyRepository.getAllTranslates().subscribe(testObserver)
        // assert & verify
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue(resultList)
    }
}