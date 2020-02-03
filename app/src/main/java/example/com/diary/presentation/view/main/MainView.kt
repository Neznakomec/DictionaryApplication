package example.com.diary.presentation.view.main

import androidx.annotation.StringRes
import example.com.diary.domain.models.Translation
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTranslationResult(result: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addToHistory(translation: Translation)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTranslations(itemViewList: List<Translation>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(error: Throwable)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(@StringRes error: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgress()
}