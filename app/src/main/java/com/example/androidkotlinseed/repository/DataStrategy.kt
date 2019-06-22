package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.api.MarvelApi

abstract class DataStrategy(open val marvelApi: MarvelApi,
                            open val cacheManager: CacheManager,
                            open val dataFactory: DataFactory) {

    interface QueryHeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    abstract fun queryHeroes(queryHeroesListener: QueryHeroesListener)
}