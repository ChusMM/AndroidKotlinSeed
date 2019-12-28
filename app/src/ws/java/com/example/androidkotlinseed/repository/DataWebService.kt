package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.disposables.Disposable

open class DataWebService(override val marvelApi: MarvelApi,
                          override val dataFactory: DataFactory,
                          override val appRxSchedulers: AppRxSchedulers,
                          override val cacheManager: CacheManager) : DataStrategy {
    private var disposable: Disposable? = null

    override fun queryHeroes(queryHeroesListener: DataStrategy.QueryHeroesListener) {
        this.cancelCurrentFetchIfActive()
        this.retrieveHeroes(queryHeroesListener)
    }

    override fun dispose() {
        this.cancelCurrentFetchIfActive()
    }

    private fun cancelCurrentFetchIfActive() {
        disposable?.dispose()
    }
}