package com.example.androidkotlinseed.repository

import android.content.Context
import com.example.androidkotlinseed.api.MarvelApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DataWebService(override val marvelApi: MarvelApi,
                     override val cacheManager: CacheManager,
                     override val dataFactory: DataFactory,
                     override val context: Context)
    : DataStrategy(marvelApi, cacheManager, dataFactory, context), CacheManager.CacheListener {

    private var disposable: Disposable? = null

    override fun queryHeroes(heroesListener: HeroesListener) {
        this.cancelCurrentFetchIfActive()

        val heroObservable = marvelApi.getHeroes()

        disposable = heroObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result -> dataFactory.superHeroesFromHeroListWrapper(result) }
            .subscribe(
                { result ->
                    run {
                        cacheManager.replaceHeroes(result, this)
                        heroesListener.onQueryHeroesOk(result)
                    }
                },
                { error ->
                    run {
                        handleError(error, heroesListener)
                        cacheManager.listHeroes(heroesListener)
                    }
                }
            )
    }

    private fun cancelCurrentFetchIfActive() {
        disposable?.dispose()
    }

    private fun handleError(error: Throwable, heroesListener: HeroesListener) {
        heroesListener.onQueryHeroesFailed(dataFactory.callErrorFromThrowable(error))
    }
}