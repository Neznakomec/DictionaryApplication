package example.com.diary.presentation.presenter.main

import example.com.diary.data.persistence.DatabaseCopyManager
import example.com.diary.presentation.presenter.BasePresenter
import io.reactivex.schedulers.Schedulers
import moxy.MvpView
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(private val databaseCopyManager: DatabaseCopyManager):
    BasePresenter<MvpView>() {
    fun copyDatabase() {
        databaseCopyManager.copyToRoot().
            subscribeOn(Schedulers.io())
            .subscribe()
    }

}