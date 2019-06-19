package com.example.androidkotlinseed.domain.usecases

import android.content.Context
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.repository.DataStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

class FetchHeroesUseCase(var dataStrategy: DataStrategy) :
    BaseUseCase<FetchHeroesUseCase.Listener>(), DataStrategy.HeroesListener {

    private var disposable: Disposable? = null

    interface Listener {
        fun onFetchHeroesOk(superHeroes: List<SuperHero>)
        fun onFetchHeroesFailed(msg: String)
    }

    constructor(dataStrategy: DataStrategy, context: Context) : this(dataStrategy) {
        super.setContextRef(context)
    }

    fun fetchAndNotify() {
        disposable?.dispose()
        dataStrategy.queryHeroes(this)
    }

    override fun onQueryHeroesOk(superHeroes: List<SuperHero>) {
        this.notifyOk(superHeroes)
    }

    override fun onQueryHeroesFailed(callError: CallError) {
        this.notifyFailed()
    }

    private fun notifyOk(superHeroes: List<SuperHero>) {
        getListener()?.let {
            disposable = Flowable.just(it)
                .subscribe { listener -> listener.onFetchHeroesOk(superHeroes) }
        }
    }

    private fun notifyFailed() {
        getListener()?.let {
            val reason = getContextRef()?.getString(R.string.server_call_failed) ?: "Connection Failed"

            disposable = Flowable.just(it)
                .subscribe { listener -> listener.onFetchHeroesFailed(reason) }
        }
    }
}