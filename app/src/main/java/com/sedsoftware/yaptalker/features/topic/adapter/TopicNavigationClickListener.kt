package com.sedsoftware.yaptalker.features.topic.adapter

interface TopicNavigationClickListener {

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()
}