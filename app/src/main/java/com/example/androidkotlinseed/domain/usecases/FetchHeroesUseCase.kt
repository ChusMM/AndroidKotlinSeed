package com.example.androidkotlinseed.domain.usecases

import android.content.Context
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.repository.DataStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FetchHeroesUseCase(var dataStrategy: DataStrategy) :
    BaseUseCase<FetchHeroesUseCase.Listener>(), DataStrategy.HeroesListener {

    private val compositeDisposable = CompositeDisposable()

    interface Listener {
        fun onFetchHeroesOk(superHeroes: List<SuperHero>)
        fun onFetchHeroesFailed(msg: String)
    }

    constructor(dataStrategy: DataStrategy, context: Context) : this(dataStrategy) {
        super.setContextRef(context)
    }

    fun fetchAndNotify() {
        dataStrategy.queryHeroes(this)
    }

    override fun onQueryHeroesOk(superHeroes: List<SuperHero>) {
        val disposable = Flowable.fromIterable(getListeners())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listener -> listener.onFetchHeroesOk(superHeroes) }

        compositeDisposable.add(disposable)
    }

    override fun onQueryHeroesFailed() {
        this.notifyFailed()
    }

    private fun notifyFailed() {
        val reason = getContextRef()?.getString(R.string.call_failed) ?: "Connection Failed"

        val disposable = Flowable.fromIterable(getListeners())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listener -> listener.onFetchHeroesFailed(reason) }

        compositeDisposable.add(disposable)
    }
}