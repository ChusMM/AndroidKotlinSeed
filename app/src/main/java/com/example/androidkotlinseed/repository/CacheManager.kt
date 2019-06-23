package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CacheManager(private val superHeroDao: SuperHeroDao,
                   private val appRxSchedulers: AppRxSchedulers) {

    companion object {
        private val TAG = CacheManager::class.simpleName
    }

    fun saveHeroes(superHeroes: List<SuperHero>): Disposable {
        return replaceHeroes(superHeroes, this::updateHeroesExpirationTimestamp)
    }

    fun queryHeroesFromCache(queryHeroesListener: DataStrategy.QueryHeroesListener): Disposable {
        return listHeroes(queryHeroesListener)
    }

    fun deleteAllHeroes(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.deleteAll()
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
            .subscribe(callback)
    }

    fun checkHeroesCacheValidity(callback: (Boolean) -> Unit): Disposable {
        return superHeroDao.getHeroesExpirationTable()
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
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
        } else {
            Log.d(TAG, "Heroes expiration row created")
        }
    }

    private fun listHeroes(listener: DataStrategy.QueryHeroesListener): Disposable {
        return superHeroDao.getAll()
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
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
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
            .subscribe(callback)
    }

    private fun insertHeroesExpirationTable(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.insertHeroesExpirationRow()
            .subscribeOn(appRxSchedulers.main)
            .observeOn(appRxSchedulers.database)
            .subscribe({ callback(1) }, { callback(0) })
    }
}