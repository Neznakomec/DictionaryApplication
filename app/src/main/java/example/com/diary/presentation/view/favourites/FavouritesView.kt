package example.com.diary.presentation.view.favourites

import example.com.diary.domain.models.Translation
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface FavouritesView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTranslations(itemViewList: List<Translation>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgress()
}