package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.domain.ExpirationTable
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.*
import io.reactivex.disposables.Disposable

class CacheManager(private val superHeroDao: SuperHeroDao,
                   private val appRxSchedulers: AppRxSchedulers) {

    companion object {
        private val TAG = CacheManager::class.simpleName
    }

    fun saveHeroes(superHeroes: List<SuperHero>): Completable {
        val handler = CompletableOnSubscribe { emitter ->
            replaceHeroes(superHeroes) { heroesRowsAffected ->
                if (heroesRowsAffected >= 0) {
                    updateHeroesExpirationTimestamp().subscribe {
                        emitter.onComplete()
                    }
                } else {
                    emitter.onError(Throwable("Heroes not inserted nor replaced"))
                }
            }
        }
        return Completable.create(handler)
    }

    fun queryHeroesFromCache(): Single<List<SuperHero>> {
        val handler = SingleOnSubscribe<List<SuperHero>> { emitter ->
            listHeroes().subscribe( {
                emitter.onSuccess(it)
            }, { error ->
                Log.e(TAG, error.toString())
                emitter.onSuccess(listOf())
            })
        }
        return Single.create(handler)
    }

    fun deleteAllHeroes(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.deleteAll()
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
            .subscribe(callback)
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
            updateHeroesExpirationTimestamp { expirationRowsAffected ->
                if (expirationRowsAffected <= 0) {
                    insertHeroesExpirationTable().subscribe {
                        emitter.onComplete()
                    }
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

    private fun replaceHeroes(superHeroes: List<SuperHero>,
                              callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.insertAll(superHeroes)
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
            .subscribe({
                callback(superHeroes.size)
            }, { error ->
                run {
                    Log.e(TAG, error.toString())
                    callback(0)
                }
            })
    }

    private fun updateHeroesExpirationTimestamp(callback: (rowsAffected: Int) -> Unit): Disposable {
        return superHeroDao.updateHeroesExpirationRow()
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
            .subscribe({ rowsUpdated ->
                run {
                    Log.d(TAG, "Heroes expiration table updated")
                    callback(rowsUpdated)
                }
            }, { error ->
                run {
                    Log.e(TAG, error.toString())
                    callback(0)
                }
            })
    }

    private fun insertHeroesExpirationTable(): Completable {
        return superHeroDao.insertHeroesExpirationRow()
            .subscribeOn(appRxSchedulers.database)
            .observeOn(appRxSchedulers.main)
            .doOnError { error ->
                Log.e(TAG, error.toString())
            }
    }
}