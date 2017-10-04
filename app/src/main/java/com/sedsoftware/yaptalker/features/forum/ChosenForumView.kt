package com.sedsoftware.yaptalker.features.forum

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.data.model.Topic

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChosenForumView : MvpView {

  fun showRefreshing()

  fun hideRefreshing()

  fun showErrorMessage(message: String)

  fun refreshTopics(topics: List<Topic>)

  fun setNavigationPagesLabel(page: Int, totalPages: Int)

  fun setIfNavigationBackEnabled(isEnabled: Boolean)

  fun setIfNavigationForwardEnabled(isEnabled: Boolean)

  fun showGoToPageDialog(maxPages: Int)

  fun showCantLoadPageMessage(page: Int)

  fun scrollToViewTop()

  fun setAppbarTitle(title: String)

  fun hideNavigationPanel()

  fun showNavigationPanel()
}
