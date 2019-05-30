package com.example.androidkotlinseed.repository

import android.content.Context
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CacheManager(private val superHeroDao: SuperHeroDao,
                   private val context: Context) {

    private val componentDisposable: CompositeDisposable = CompositeDisposable()

    interface CacheListener {
        fun onCacheOperationFinish(rowsAffected: Int)
    }

    fun listHeroes(listener: DataStrategy.HeroesListener): Disposable {
        return superHeroDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener::onQueryHeroesOk)
    }

    fun deleteAllHeroes(cacheListener: CacheListener): Disposable {
        return superHeroDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(cacheListener::onCacheOperationFinish)
    }

    fun replaceHeroes(superHeroes: List<SuperHero>, cacheListener: CacheListener): Disposable {
        return superHeroDao.insertAll(superHeroes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { cacheListener.onCacheOperationFinish(0) }
    }
}