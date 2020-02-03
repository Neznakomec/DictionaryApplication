package example.com.diary.presentation.presenter.main

import example.com.diary.utils.schedulers.RxTestSchedulers
import example.com.diary.utils.schedulers.SchedulerProvider
import example.com.diary.domain.models.Result
import example.com.diary.domain.models.Translation
import example.com.diary.domain.history.HistoryInteractor
import example.com.diary.presentation.view.main.MainView
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import ru.terrakok.cicerone.Router

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    lateinit var historyInteractor: HistoryInteractor
    @Mock
    lateinit var router: Router
    @InjectMocks
    lateinit var presenter: MainPresenter
    @Spy
    private val schedulers: SchedulerProvider = RxTestSchedulers()
    @Mock
    lateinit var mockView: MainView

    @Before
    fun beforeEachTest() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(historyInteractor, router, schedulers)
    }

    @Test
    fun `addTranslationToHistory adds item and shows it on view`() {
        // when
        val correctTranslation =
            Translation(
                langFrom = "en", langTo = "ru",
                textFrom = "apple", textTo = "яблоко",
                isFavourite = false
            )

        val result: Result.Success<Translation> = Result.Success(correctTranslation)
        `when`(historyInteractor.getAllTranslates()).thenReturn(Maybe.just(listOf(correctTranslation)))
        `when`(historyInteractor.addTranslation(translation = correctTranslation)).thenReturn(Completable.fromAction {})
        // action
        presenter.attachView(mockView)
        presenter.onSuccessTranslation(result)
        presenter.addTranslationToHistory()
        // assert & verify
        verify(historyInteractor, times(1)).addTranslation(correctTranslation)
        verify(mockView, times(1)).addToHistory(correctTranslation)
    }
}