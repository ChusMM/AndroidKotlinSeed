package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.disposables.Disposable

interface DataStrategy {
    companion object {
        private val TAG = DataStrategy::class.java.simpleName
    }

    val marvelApi: MarvelApi
    val appRxSchedulers: AppRxSchedulers
    val cacheManager: CacheManager
    val dataFactory: DataFactory

    interface QueryHeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    fun queryHeroes(queryHeroesListener: QueryHeroesListener)

    fun dispose()

    fun retrieveHeroes(queryHeroesListener: QueryHeroesListener): Disposable {
        val heroObservable = marvelApi.getHeroes()
        return heroObservable.subscribeOn(appRxSchedulers.network)
                .observeOn(appRxSchedulers.main)
                .map { result -> dataFactory.superHeroesFromHeroListWrapper(result) }
                .subscribe(
                        { result -> saveHeroesRetrieved(result, queryHeroesListener) },
                        { error -> onGetHeroesErrorHandler(queryHeroesListener, error) }
                )
    }

    fun saveHeroesRetrieved(result: List<SuperHero>, queryHeroesListener: QueryHeroesListener): Disposable {
        return cacheManager.saveHeroes(result)
                .subscribe({
                    queryHeroesListener.onQueryHeroesOk(result)
                }, {
                    Log.e(TAG, it.toString())
                    queryHeroesListener.onQueryHeroesOk(result)
                })
    }

    fun onGetHeroesErrorHandler(queryHeroesListener: QueryHeroesListener, error: Throwable): Disposable {
        return cacheManager.checkHeroesCacheValidity().subscribe { cacheExpired ->
            if (!cacheExpired) {
                cacheManager.queryHeroesFromCache()
                        .subscribe({
                            queryHeroesListener.onQueryHeroesOk(it)
                        }, {
                            Log.e(TAG, it.toString())
                            queryHeroesListener.onQueryHeroesFailed(dataFactory.callErrorFromThrowable(it))
                        })
            } else {

                queryHeroesListener.onQueryHeroesFailed(dataFactory.callErrorFromThrowable(error))
            }
        }
    }
}