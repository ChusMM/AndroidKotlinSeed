package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.disposables.Disposable

class DataWebService(private val marvelApi: MarvelApi,
                     override val dataFactory: DataFactory,
                     private val appRxSchedulers: AppRxSchedulers,
                     override val cacheManager: CacheManager) : DataStrategy {

    private var disposable: Disposable? = null

    override fun queryHeroes(queryHeroesListener: DataStrategy.QueryHeroesListener) {
        this.cancelCurrentFetchIfActive()

        val heroObservable = marvelApi.getHeroes()

        disposable = heroObservable.subscribeOn(appRxSchedulers.network)
            .observeOn(appRxSchedulers.main)
            .map { result -> dataFactory.superHeroesFromHeroListWrapper(result) }
            .subscribe(
                { result -> saveHeroesRetrieved(result, queryHeroesListener) },
                { error -> handleGetHeroesError(queryHeroesListener, dataFactory.callErrorFromThrowable(error)) }
            )
    }

    private fun cancelCurrentFetchIfActive() {
        disposable?.dispose()
    }
}