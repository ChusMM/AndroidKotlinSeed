package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CacheManager(private val superHeroDao: SuperHeroDao) {

    companion object {
        private val TAG = CacheManager::class.simpleName
    }

    private val compositeDisposable = CompositeDisposable()

    fun saveHeroes(superHeroes: List<SuperHero>) {
        val disposable = replaceHeroes(superHeroes, this::updateHeroesExpirationTimestamp)
        compositeDisposable.add(disposable)
    }

    fun queryHeroesFromCache(queryHeroesListener: DataStrategy.QueryHeroesListener) {
        val disposable = listHeroes(queryHeroesListener)
        compositeDisposable.add(disposable)
    }

    fun deleteAllHeroes(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback)
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

    private fun updateHeroesExpirationTimestamp(rowsAffected: Int) {
        if (rowsAffected > 0) {
            val disposable = updateHeroesExpirationTimestamp(this::onUpdateHeroesExpirationTimestamp)
            compositeDisposable.add(disposable)
        } else {
            Log.e(TAG, "Could not replace/insert heroes list")
        }
    }

    private fun onUpdateHeroesExpirationTimestamp(rowsAffected: Int) {
        if (rowsAffected <= 0) {
            insertHeroesExpirationTable(this::onInsertHeroesExpirationTimestamp)
        } else {
            Log.d(TAG, "Heroes expiration table updated")
        }
    }

    private fun onInsertHeroesExpirationTimestamp(rowsAffected: Int) {
        if (rowsAffected <= 0) {
            Log.e(TAG, "Could not create heroes expiration table")
        }
    }

    private fun listHeroes(listener: DataStrategy.QueryHeroesListener): Disposable {
        return superHeroDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener::onQueryHeroesOk)
    }

    private fun replaceHeroes(superHeroes: List<SuperHero>, callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.insertAll(superHeroes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callback(superHeroes.size) },  { callback(0) })
    }

    private fun updateHeroesExpirationTimestamp(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.updateHeroesExpirationRow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback)
    }

    private fun insertHeroesExpirationTable(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.insertHeroesExpirationRow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callback(1) }, { callback(0) })
    }
}