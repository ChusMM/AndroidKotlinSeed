package com.example.androidkotlinseed.repository

import android.content.Context
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.api.MarvelApi

abstract class DataStrategy(open val marvelApi: MarvelApi,
                            open val cacheManager: CacheManager,
                            open val dataFactory: DataFactory,
                            open val context: Context) {

    interface HeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    abstract fun queryHeroes(heroesListener: HeroesListener)

    protected fun saveHeroes(superHeroes: List<SuperHero>, listener: CacheManager.CacheListener) {
        this.cacheManager.replaceHeroes(superHeroes, listener)
    }
}