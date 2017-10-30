package com.sedsoftware.yaptalker.features.forumslist.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.data.model.ForumItem

class ForumsAdapter(itemClick: ForumsItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<ViewType>
  private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

  init {
    delegateAdapters.put(ContentTypes.FORUM, ForumsDelegateAdapter(itemClick))
    items = ArrayList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return delegateAdapters.get(viewType).onCreateViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
  }

  override fun getItemViewType(position: Int): Int {
    return items[position].getViewType()
  }

  override fun getItemCount() = items.size

  override fun getItemId(position: Int) = position.toLong()

  fun getForums(): List<ForumItem> {
    return items
        .filter { it.getViewType() == ContentTypes.FORUM }
        .map { it as ForumItem }
  }

  fun addForumsListItem(item: ForumItem) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun addForumsList(list: List<ForumItem>) {
    items.clear()
    items.addAll(list)
    notifyDataSetChanged()
  }

  fun clearForumsList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}
