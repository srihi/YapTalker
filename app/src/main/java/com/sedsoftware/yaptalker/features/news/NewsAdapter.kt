package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.getShortTime
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.commons.parseLink
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.remote.thumbnails.ThumbnailsLoader
import kotlinx.android.synthetic.main.controller_news_item.view.*
import java.util.ArrayList
import java.util.Locale
import javax.inject.Inject

class NewsAdapter(
    val itemClick: (String, String) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var thumbnailsLoader: ThumbnailsLoader

  private var news: ArrayList<NewsItem> = ArrayList()
  private var lastPosition = -1

  override fun getItemCount() = news.size

  override fun getItemId(position: Int) = news[position].link.getLastDigits().toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_news_item, parent,
        false)
    return NewsViewHolder(view, itemClick)
  }

  override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
    holder.bindTo(news[position])

    val animation = AnimationUtils.loadAnimation(holder.itemView.context,
        if (position > lastPosition)
          R.anim.recyclerview_up_from_bottom
        else
          R.anim.recyclerview_down_from_top)

    holder.itemView.startAnimation(animation)
    lastPosition = holder.adapterPosition
  }

  override fun onViewDetachedFromWindow(holder: NewsViewHolder?) {
    super.onViewDetachedFromWindow(holder)
    holder?.itemView?.clearAnimation()
  }

  fun addNewsItem(item: NewsItem) {
    val insertPosition = news.size
    news.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearNews() {
    notifyItemRangeRemoved(0, news.size)
    news.clear()
  }

  inner class NewsViewHolder(
      itemView: View, val itemClick: (String, String) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val forumTitleTemplate: String = itemView.context.getString(
        R.string.news_forum_title_template)
    private val karmaTemplate: String = itemView.context.getString(R.string.news_karma_template)
    private val commentsTemplate: String = itemView.context.getString(
        R.string.news_comments_template)

    fun bindTo(newsItem: NewsItem) {
      with(newsItem) {
        with(itemView) {
          news_author.text = author
          news_title.text = title
          news_forum.text = String.format(Locale.US, forumTitleTemplate, forumName)
          news_date.text = context.getShortTime(date)

          if (rating.isNotEmpty()) {
            news_rating.text = String.format(Locale.US, karmaTemplate, rating)
          }

          news_comments_counter.text = String.format(Locale.US, commentsTemplate, comments)
          news_content_text.textFromHtml(cleanedDescription)

          when {
            images.isNotEmpty() -> {
              news_content_image.visibility = View.VISIBLE
              news_content_image.loadFromUrl("http:${images.first()}")
            }
            videos.isNotEmpty() -> {
              news_content_image.visibility = View.VISIBLE
              thumbnailsLoader.loadThumbnail(parseLink(videos.first()), news_content_image)
            }
            else -> news_content_image.visibility = View.GONE
          }

          setOnClickListener { itemClick(link, forumLink) }
        }
      }
    }
  }
}

