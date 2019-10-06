package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import io.reactivex.disposables.Disposable

interface DataStrategy {
    companion object {
        private val TAG = DataStrategy::class.java.simpleName
    }

    val cacheManager: CacheManager

    interface QueryHeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    fun queryHeroes(queryHeroesListener: QueryHeroesListener)

    fun saveHeroesRetrieved(result: List<SuperHero>, queryHeroesListener: QueryHeroesListener): Disposable {
        return cacheManager.saveHeroes(result)
            .doOnError { error ->
                Log.e(TAG, error.toString())
            }.subscribe({
                queryHeroesListener.onQueryHeroesOk(result)
            }, {
                queryHeroesListener.onQueryHeroesOk(result)
            })
    }

    fun handleGetHeroesError(queryHeroesListener: QueryHeroesListener, callError: CallError): Disposable {
        return cacheManager.checkHeroesCacheValidity().subscribe { cacheExpired ->
            if (!cacheExpired) {
                cacheManager.queryHeroesFromCache()
                    .subscribe({
                        queryHeroesListener.onQueryHeroesOk(it)
                    }, {
                        queryHeroesListener.onQueryHeroesFailed(
                            CallError.buildFromErrorCode(CallError.UNKNOWN_ERROR.errorCode))
                    })
            } else {
                queryHeroesListener.onQueryHeroesFailed(callError)
            }
        }
    }
}