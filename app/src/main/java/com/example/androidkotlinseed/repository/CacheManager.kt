package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.domain.ExpirationTable
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single

class CacheManager(private val superHeroDao: SuperHeroDao,
                   private val appRxSchedulers: AppRxSchedulers) {

    companion object {
        private val TAG = CacheManager::class.simpleName
    }

    fun saveHeroes(superHeroes: List<SuperHero>): Completable {
        val handler = CompletableOnSubscribe { emitter ->
            replaceHeroes(superHeroes)
                    .doOnError(emitter::onError)
                    .subscribe {
                        updateHeroesExpirationTimestamp()
                                .doOnError(emitter::onError)
                                .subscribe(emitter::onComplete)
                    }
        }
        return Completable.create(handler)
    }

    fun queryHeroesFromCache(): Observable<List<SuperHero>> {
        return listHeroes()
    }

    fun checkHeroesCacheValidity(): Observable<Boolean> {
        val handler = ObservableOnSubscribe<Boolean> { emitter ->
            getHeroesExpirationsTable().subscribe({
                emitter.onNext(it.isExpired())
            }, { error ->
                Log.e(TAG, error.toString())
                emitter.onNext(true)
            })
        }
        return Observable.create(handler)
    }

    private fun getHeroesExpirationsTable(): Single<ExpirationTable> {
        return superHeroDao.getHeroesExpirationTable()
                .subscribeOn(appRxSchedulers.database)
                .observeOn(appRxSchedulers.main)
    }

    private fun updateHeroesExpirationTimestamp(): Completable {
        return Completable.create { emitter ->
            updateHeroesExpirationTable()
                    .doOnError(emitter::onError)
                    .subscribe { expirationRowsAffected ->
                        if (expirationRowsAffected <= 0) {
                            insertHeroesExpirationTable()
                                    .doOnError(emitter::onError)
                                    .subscribe(emitter::onComplete)
                        } else {
                            emitter.onComplete()
                        }
                    }
        }
    }

    private fun listHeroes(): Observable<List<SuperHero>> {
        return superHeroDao.getAll()
                .subscribeOn(appRxSchedulers.database)
                .observeOn(appRxSchedulers.main)
    }

    private fun deleteAllHeroes(): Single<Int> {
        return superHeroDao.deleteAll()
                .subscribeOn(appRxSchedulers.database)
                .observeOn(appRxSchedulers.main)
    }

    private fun replaceHeroes(superHeroes: List<SuperHero>): Completable {
        return superHeroDao.insertAll(superHeroes)
                .subscribeOn(appRxSchedulers.database)
                .observeOn(appRxSchedulers.main)
    }

    private fun updateHeroesExpirationTable(): Single<Int> {
        return superHeroDao.updateHeroesExpirationRow()
                .subscribeOn(appRxSchedulers.database)
                .observeOn(appRxSchedulers.main)
    }

    private fun insertHeroesExpirationTable(): Completable {
        return superHeroDao.insertHeroesExpirationRow()
                .subscribeOn(appRxSchedulers.database)
                .observeOn(appRxSchedulers.main)
    }
}