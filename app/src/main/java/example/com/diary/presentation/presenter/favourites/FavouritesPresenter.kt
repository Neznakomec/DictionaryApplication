package example.com.diary.presentation.presenter.favourites

import android.util.Log
import example.com.diary.domain.models.Translation
import example.com.diary.domain.favourite.FavouritesInteractor
import example.com.diary.presentation.presenter.BasePresenter
import example.com.diary.presentation.view.favourites.FavouritesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class FavouritesPresenter @Inject constructor(
    private val favouriteсInteractor: FavouritesInteractor,
    private val router: Router) : BasePresenter<FavouritesView>() {

    override fun onFirstViewAttach() {
        connect(
            favouriteсInteractor.getFavouriteTranslates()
                .doOnSubscribe { viewState.showProgress() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { viewState.hideProgress() }
                .subscribe( {list -> viewState.setTranslations(list)},
                    this ::onErrorLoadingList )
        )
    }

    fun onBackPressed() {
        router.exit()
    }

    private fun onErrorLoadingList(throwable: Throwable) {
        Log.d("translate", "loading list error " + throwable.localizedMessage)
    }

    fun writeTranslationToHistory(translation: Translation?) {
        translation?.let {
            favouriteсInteractor.addTranslation(translation)
                .subscribeOn(Schedulers.io())
                .subscribe({}, this::onErrorLoadingList)

        }
    }

}