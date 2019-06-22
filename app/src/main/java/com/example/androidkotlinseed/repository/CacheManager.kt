package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CacheManager(private val superHeroDao: SuperHeroDao) {

    companion object {
        private val TAG = CacheManager::class.simpleName
    }

    fun listHeroes(listener: DataStrategy.HeroesListener): Disposable {
        return superHeroDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener::onQueryHeroesOk)
    }

    fun deleteAllHeroes(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback)
    }

    fun replaceHeroes(superHeroes: List<SuperHero>, callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.insertAll(superHeroes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callback(superHeroes.size) },  { callback(0) })
    }

    fun updateHeroesExpirationTable(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.updateHeroesExpirationRow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback)
    }

    fun insertHeroesExpirationTable(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.insertHeroesExpirationRow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callback(1) }, { callback(0) })
    }

    fun checkHeroesCacheValidity(callback: (Boolean) -> Unit): Disposable {
        return superHeroDao.getHeroesExpirationTable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                callback(result.isExpired())
            }, { error ->
                Log.e(TAG, error.toString())
                callback(true)
            })
    }
}