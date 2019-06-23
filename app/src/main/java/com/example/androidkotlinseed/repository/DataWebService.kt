package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.disposables.Disposable

class DataWebService(private val marvelApi: MarvelApi,
                     private val dataFactory: DataFactory,
                     private val cacheManager: CacheManager,
                     private val appRxSchedulers: AppRxSchedulers) : DataStrategy {

    private var disposable: Disposable? = null

    override fun queryHeroes(queryHeroesListener: DataStrategy.QueryHeroesListener) {
        this.cancelCurrentFetchIfActive()

        val heroObservable = marvelApi.getHeroes()

        disposable = heroObservable.subscribeOn(appRxSchedulers.network)
            .observeOn(appRxSchedulers.main)
            .map { result -> dataFactory.superHeroesFromHeroListWrapper(result) }
            .subscribe(
                { result ->
                    run {
                        cacheManager.saveHeroes(result)
                        queryHeroesListener.onQueryHeroesOk(result)
                    }
                },
                { error ->
                    run {
                        cacheManager.checkHeroesCacheValidity {
                                cacheExpired -> handleError(cacheExpired, queryHeroesListener, error)
                        }
                    }
                }
            )
    }

    private fun cancelCurrentFetchIfActive() {
        disposable?.dispose()
    }

    private fun handleError(cacheExpired: Boolean,
                            queryHeroesListener: DataStrategy.QueryHeroesListener,
                            error: Throwable) {
        if (cacheExpired) {
            queryHeroesListener.onQueryHeroesFailed(dataFactory.callErrorFromThrowable(error))
        } else {
            cacheManager.queryHeroesFromCache(queryHeroesListener)
        }
    }
}