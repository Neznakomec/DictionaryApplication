package example.com.diary.presentation.presenter.main

import android.util.Log
import example.com.diary.R
import example.com.diary.utils.schedulers.SchedulerProvider
import example.com.diary.domain.models.RequestToTranslate
import example.com.diary.domain.models.Result
import example.com.diary.domain.models.Translation
import example.com.diary.domain.history.HistoryInteractor
import example.com.diary.presentation.Screens
import example.com.diary.presentation.presenter.BasePresenter
import example.com.diary.presentation.view.main.MainView
import io.reactivex.Maybe
import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class MainPresenter @Inject constructor(
    private val historyInteractor: HistoryInteractor,
    private val router: Router,
    private val schedulers: SchedulerProvider) : BasePresenter<MainView>() {

    private var lastTranslation: Translation? = null

    override fun attachView(view: MainView) {
        super.attachView(view)
        connect(
            historyInteractor.getAllTranslates()
            .doOnSubscribe { viewState.showProgress() }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.mainThread)
            .doFinally { viewState.hideProgress() }
            .subscribe( {list -> viewState.setTranslations(list)},
                this ::onErrorLoadingList )
        )
    }

    fun onFavouritesClicked() {
        router.navigateTo(Screens.FavouritesScreen())
    }

    fun doTranslation(text: String, langFrom: String, langTo: String) {
        connect(
            historyInteractor.translateText(
                RequestToTranslate(
                    text,
                    langFrom,
                    langTo
                )
            )
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.mainThread)
            .subscribe({ result: Result<Translation> ->
                when (result) {
                    is Result.Success -> onSuccessTranslation(result)
                    is Result.Failure -> onFailTranslation(result)
                }
            }, this::onErrorTranslation
            )
        )
    }

    fun deleteTranslation(translation: Translation) {
        historyInteractor.deleteTranslation(translation)
            .subscribeOn(schedulers.io)
            .subscribe()
    }

    fun onSuccessTranslation(result: Result.Success<Translation>) {
        val translation = result.value
        if (translation.textFrom.isNotEmpty()) {
            lastTranslation = translation
        }
        viewState.showTranslationResult(translation.textTo)
    }

    private fun onFailTranslation(result: Result.Failure<Translation>) {
        Log.d("translate", "translation error, code ${result.code}")
        viewState.showError(result.exception)
    }

    private fun onErrorTranslation(throwable: Throwable) {
        Log.d("translate", "translate error " + throwable.localizedMessage)
    }

    private fun onErrorLoadingList(throwable: Throwable) {
        Log.d("translate", "loading list error " + throwable.localizedMessage)
    }

    fun translationItemFavouritesClick(translation: Translation) {
        translation.isFavourite = !translation.isFavourite
        connect(
            writeTranslationToHistory(translation)
                .subscribe({}, this::onErrorLoadingList)
        )
    }

    fun addTranslationToHistory() {
        connect(
            writeTranslationToHistory()
                .observeOn(schedulers.mainThread)
                .subscribe({
                    viewState.addToHistory(it)
                }, this::onErrorLoadingList)
        )
    }

    private fun writeTranslationToHistory(translation: Translation? = lastTranslation): Maybe<Translation> {
        translation?.let {
            return historyInteractor.addTranslation(translation)
                .subscribeOn(schedulers.io)
                .andThen(Maybe.just(it))
        } ?: viewState.showError(R.string.no_translated_text)

        return Maybe.empty()
    }
}