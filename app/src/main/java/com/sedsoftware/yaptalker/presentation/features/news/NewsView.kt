package com.sedsoftware.yaptalker.presentation.features.news

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendNewsItem(entity: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearNewsList()

  fun showFab()

  fun hideFab()
}
