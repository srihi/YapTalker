package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapSearchIdLoader
import com.sedsoftware.yaptalker.domain.repository.SearchIdRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapSearchIdRepository @Inject constructor(
    private val searchIdLoader: YapSearchIdLoader
) : SearchIdRepository {

  override fun getSearchId(): Observable<String> =
      searchIdLoader
          .loadSearchIdHash()
}
