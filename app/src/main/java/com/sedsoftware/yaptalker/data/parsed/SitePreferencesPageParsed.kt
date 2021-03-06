package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class SitePreferencesPageParsed {
  @Selector("select[name=postpage] option[selected=selected]", attr = "value", defValue = "25")
  lateinit var messagesPerTopicPage: String
  @Selector("select[name=topicpage] option[selected=selected]", attr = "value", defValue = "30")
  lateinit var topicsPerForumPage: String
}
