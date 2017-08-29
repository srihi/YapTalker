package com.sedsoftware.yaptalker.features.base

import android.support.annotation.NonNull
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.data.remote.yap.YapRequestState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

  private val subscriptions by lazy { CompositeDisposable() }

  protected fun unsubscribeOnDestroy(@NonNull subscription: Disposable) {
    subscriptions.add(subscription)
  }

  override fun onDestroy() {
    super.onDestroy()
    subscriptions.clear()
  }

  fun pushAppbarTitle(channel: BehaviorRelay<String>, title: String) {
    Observable.just(title)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(channel)
  }

  protected fun attachRefreshIndicator(requestState: BehaviorRelay<Long>,
      onLoadingStart: () -> Unit, onLoadingFinish: () -> Unit) {

    requestState
        .subscribe { state: Long ->
          when (state) {
            YapRequestState.LOADING -> {
              onLoadingStart()
            }
            YapRequestState.COMPLETED,
            YapRequestState.ERROR -> {
              onLoadingFinish()
            }
          }
        }
        .apply { unsubscribeOnDestroy(this) }
  }
}
