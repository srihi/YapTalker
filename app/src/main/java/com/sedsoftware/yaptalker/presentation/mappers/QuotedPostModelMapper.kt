package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.QuotedPostModel
import javax.inject.Inject

/**
 * Mapper class used to transform quoted post text from the domain layer into YapEntity in the presentation layer.
 */
class QuotedPostModelMapper @Inject constructor() {

  fun transform(item: BaseEntity): YapEntity {

    item as QuotedPost

    return QuotedPostModel(text = item.text)
  }
}
