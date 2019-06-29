package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero

interface DataStrategy {
    val cacheManager: CacheManager

    interface QueryHeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    fun queryHeroes(queryHeroesListener: QueryHeroesListener)

    fun saveHeroesRetrieved(result: List<SuperHero>, queryHeroesListener: QueryHeroesListener) {
        cacheManager.saveHeroes(result)
        queryHeroesListener.onQueryHeroesOk(result)
    }

    fun handleGetHeroesError(queryHeroesListener: QueryHeroesListener, callError: CallError) {
        cacheManager.checkHeroesCacheValidity { cacheExpired ->
            evaluateHeroesCacheAccess(cacheExpired, queryHeroesListener, callError)
        }
    }

    fun evaluateHeroesCacheAccess(cacheExpired: Boolean, queryHeroesListener: QueryHeroesListener, callError: CallError) {
        if (cacheExpired) {
            queryHeroesListener.onQueryHeroesFailed(callError)
        } else {
            cacheManager.queryHeroesFromCache(queryHeroesListener)
        }
    }
}