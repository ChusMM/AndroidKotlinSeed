package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.api.MarvelApi
import io.reactivex.disposables.CompositeDisposable

abstract class DataStrategy(open val marvelApi: MarvelApi,
                            open val cacheManager: CacheManager,
                            open val dataFactory: DataFactory) {

    companion object {
        private val TAG = DataStrategy::class.simpleName
    }

    interface HeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    private val compositeDisposable = CompositeDisposable()

    abstract fun queryHeroes(heroesListener: HeroesListener)

    protected fun saveHeroes(superHeroes: List<SuperHero>) {
        val disposable = cacheManager.replaceHeroes(superHeroes, this::updateHeroesExpirationTable)
        compositeDisposable.add(disposable)
    }

    fun queryHeroesFromCache(heroesListener: HeroesListener) {
        val disposable = cacheManager.listHeroes(heroesListener)
        compositeDisposable.add(disposable)
    }

    private fun updateHeroesExpirationTable(rowsAffected: Int) {
        val disposable = cacheManager.updateHeroesExpirationTable(this::onUpdateExpirationTableFinish)
        compositeDisposable.add(disposable)
    }

    private fun onUpdateExpirationTableFinish(rowsAffected: Int) {
        if (rowsAffected <= 0) {
            cacheManager.insertHeroesExpirationTable(this::onInsertExpirationTableFinish)
        } else {
            Log.d(TAG, "Heroes expiration table updated")
        }
    }

    private fun onInsertExpirationTableFinish(rowsAffected: Int) {
        if (rowsAffected <= 0) {
            Log.e(TAG, "Could not create heroes expiration table")
        }
    }
}