package com.sedsoftware.yaptalker.features.forum

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.model.Topic
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ChosenForumPresenter : BasePresenter<ChosenForumView>() {

  companion object {
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val RATING_SORTER = "rank"
    private const val TOPICS_PER_PAGE = 30
    private const val OFFSET_FOR_PAGE_NUMBER = 1
  }

  private var currentForumId = 0
  private var currentSorting = LAST_UPDATE_SORTER
  private var currentPage = 0
  private var totalPages = -1
  private var currentTitle = ""

  override fun attachView(view: ChosenForumView?) {
    super.attachView(view)
    viewState.hideNavigationPanel()
  }

  fun checkSavedState(forumId: Int, savedViewState: Bundle?, key: String) {
    if (savedViewState != null && savedViewState.containsKey(key)) {
      val topics = savedViewState.getParcelableArrayList<Topic>(key)
      onRestoringSuccess(topics)
    } else {
      loadForum(forumId)
    }
  }

  fun goToNextPage() {
    if (currentPage in 0 until totalPages - 1) {
      currentPage++
      loadForumCurrentPage()
    }
  }

  fun goToPreviousPage() {
    if (currentPage in 1 until totalPages) {
      currentPage--
      loadForumCurrentPage()
    }
  }

  fun goToChosenPage() {
    viewState.showGoToPageDialog(totalPages)
  }

  fun loadForum(forumId: Int, page: Int = 0, sortByRank: Boolean = false) {
    currentForumId = forumId
    currentPage = page
    currentSorting =
        if (sortByRank) RATING_SORTER
        else LAST_UPDATE_SORTER

    loadForumCurrentPage()
  }

  fun loadChosenForumPage(chosenPage: Int) {
    if (chosenPage in 1..totalPages) {
      currentPage = chosenPage - OFFSET_FOR_PAGE_NUMBER
      loadForumCurrentPage()
    } else {
      viewState.showCantLoadPageMessage(chosenPage)
    }
  }

  fun handleNavigationVisibility(isNavigationShown: Boolean, diff: Int) {
    when {
      isNavigationShown && diff > 0 -> viewState.hideNavigationPanel()
      !isNavigationShown && diff < 0 -> viewState.showNavigationPanel()
    }
  }

  private fun loadForumCurrentPage() {

    val startingTopic = currentPage * TOPICS_PER_PAGE

    yapDataManager
        .getChosenForum(currentForumId, startingTopic, currentSorting)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          forumPage ->
          onLoadingSuccess(forumPage)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun setNavigationLabel() {
    viewState.setNavigationPagesLabel(currentPage + OFFSET_FOR_PAGE_NUMBER, totalPages)
  }

  private fun setNavigationAvailability() {

    var backNavigationAvailable = true
    var forwardNavigationAvailable = true

    when (currentPage) {
      0 -> backNavigationAvailable = false
      totalPages - OFFSET_FOR_PAGE_NUMBER -> forwardNavigationAvailable = false
    }

    viewState.setIfNavigationBackEnabled(backNavigationAvailable)
    viewState.setIfNavigationForwardEnabled(forwardNavigationAvailable)
  }

  private fun onLoadingSuccess(forumPage: ForumPage) {
    totalPages = forumPage.totalPages.toInt()
    currentTitle = forumPage.forumTitle
    viewState.refreshTopics(forumPage.topics)
    viewState.scrollToViewTop()
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onRestoringSuccess(topics: List<Topic>) {
    viewState.refreshTopics(topics)
    setNavigationLabel()
    setNavigationAvailability()
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
