package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.api.MarvelApi
import io.reactivex.disposables.CompositeDisposable

abstract class DataStrategy(open val marvelApi: MarvelApi,
                            open val cacheManager: CacheManager,
                            open val dataFactory: DataFactory) : CacheManager.CacheListener {

    interface HeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    private val compositeDisposable = CompositeDisposable()

    abstract fun queryHeroes(heroesListener: HeroesListener)

    protected fun saveHeroes(superHeroes: List<SuperHero>) {
        val disposable = cacheManager.replaceHeroes(superHeroes, this)
        compositeDisposable.add(disposable)
    }

    override fun onCacheOperationFinish(rowsAffected: Int) { }

    fun queryHeroesFromCache(heroesListener: HeroesListener) {
        val disposable = cacheManager.listHeroes(heroesListener)
        compositeDisposable.add(disposable)
    }
}