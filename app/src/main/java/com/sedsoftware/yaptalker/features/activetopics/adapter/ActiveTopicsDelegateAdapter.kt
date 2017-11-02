package com.sedsoftware.yaptalker.features.activetopics.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseAdapterInjections
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.inflate
import com.sedsoftware.yaptalker.commons.extensions.textColor
import com.sedsoftware.yaptalker.data.parsing.ActiveTopic
import kotlinx.android.synthetic.main.fragment_active_topics_list_item.view.*
import java.util.Locale

class ActiveTopicsDelegateAdapter(val itemClick: ActiveTopicsItemClickListener) :
    BaseAdapterInjections(), ViewTypeDelegateAdapter {

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return TopicViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ViewType) {
    holder as TopicViewHolder
    holder.bindTo(item as ActiveTopic)
  }

  inner class TopicViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_active_topics_list_item)) {

    private val commentsTemplate = parent.context.getString(R.string.forum_comments_template)
    private val pinnedTopicTemplate = parent.context.getString(R.string.forum_pinned_topic_template)
    private val closedTopicTemplate = parent.context.getString(R.string.forum_closed_topic_template)
    private val pinnedAndClosedTopicTemplate = parent.context.getString(R.string.forum_pinned_and_closed_topic_template)

    fun bindTo(topicItem: ActiveTopic) {
      with(topicItem) {
        with(itemView) {

          val topicPinned = isPinned.isNotEmpty()
          val topicClosed = isClosed.isNotEmpty()

          when {
            topicPinned && topicClosed -> active_topic_name.text = String.format(Locale.getDefault(),
                pinnedAndClosedTopicTemplate, title)

            topicPinned && !topicClosed -> active_topic_name.text = String.format(Locale.getDefault(),
                pinnedTopicTemplate, title)

            !topicPinned && topicClosed -> active_topic_name.text = String.format(Locale.getDefault(),
                closedTopicTemplate, title)

            else -> active_topic_name.text = title
          }

          active_topic_forum.text = forumTitle
          active_topic_last_post_date.shortDateText = lastPostDate
          active_topic_answers.text = String.format(Locale.getDefault(), commentsTemplate, answers)
          active_topic_rating.text = rating

          if (rating.isNotEmpty()) {
            active_topic_rating.loadRatingBackground(rating.toInt())
          }

          active_topic_name.textSize = normalFontSize
          active_topic_rating.textSize = normalFontSize

          setOnClickListener { itemClick.onTopicClick(forumLink.getLastDigits(), link.getLastDigits()) }
        }
      }
    }
  }

  @Suppress("MagicNumber")
  private fun TextView.loadRatingBackground(rating: Int) {

    when (rating) {
    // Platinum
      in 1000..50000 -> {
        textColor = R.color.colorRatingPlatinumText
        setBackgroundResource(R.drawable.topic_rating_platinum)
      }
    // Gold
      in 500..999 -> {
        textColor = R.color.colorRatingGoldText
        setBackgroundResource(R.drawable.topic_rating_gold)
      }
    // Green
      in 50..499 -> {
        textColor = R.color.colorRatingGreenText
        setBackgroundResource(R.drawable.topic_rating_green)
      }
    // Gray
      in -9..49 -> {
        textColor = R.color.colorRatingGreyText
        setBackgroundResource(R.drawable.topic_rating_grey)
      }
    // Red
      in -99..-10 -> {
        textColor = R.color.colorRatingRedText
        setBackgroundResource(R.drawable.topic_rating_red)
      }
    // Dark Red
      else -> {
        textColor = R.color.colorRatingDarkRedText
        setBackgroundResource(R.drawable.topic_rating_dark_red)
      }
    }
  }
}