package com.example.androidkotlinseed.repository

import android.content.Context
import com.example.androidkotlinseed.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CacheManager(private val superHeroDao: SuperHeroDao,
                   private val context: Context) {

    private val componentDisposable: CompositeDisposable = CompositeDisposable()

    interface CacheListener {
        fun onOperationFinish(rowsAffected: Int) { }
    }

    fun listHeroes(listener: DataStrategy.HeroesListener) {
        val disposable = superHeroDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener::onQueryHeroesOk)
        componentDisposable.add(disposable)
    }

    fun deleteAllHeroes(cacheListener: CacheListener) {
        val disposable = superHeroDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(cacheListener::onOperationFinish)
        componentDisposable.add(disposable)
    }

    fun replaceHeroes(superHeroes: List<SuperHero>, cacheListener: CacheListener) {
        val disposable = superHeroDao.insertAll(superHeroes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { cacheListener.onOperationFinish(0) }
        componentDisposable.add(disposable)
    }
}