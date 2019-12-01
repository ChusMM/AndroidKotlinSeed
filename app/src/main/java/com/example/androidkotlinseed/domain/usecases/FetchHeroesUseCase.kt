package com.example.androidkotlinseed.domain.usecases

import android.content.Context
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.repository.DataStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class FetchHeroesUseCase(private val dataStrategy: DataStrategy) : IFetchHeroesUseCase {

    override var contextRef: WeakReference<Context>? = null
    override var listener: IFetchHeroesUseCase.Listener? = null

    private var disposable: Disposable? = null

    constructor(dataStrategy: DataStrategy, context: Context) : this(dataStrategy) {
        setContextRef(context)
    }

    override fun fetchAndNotify() {
        this.dispose()
        dataStrategy.queryHeroes(this)
    }

    override fun dispose() {
        disposable?.dispose()
        dataStrategy.dispose()
    }

    override fun onQueryHeroesOk(superHeroes: List<SuperHero>) {
        this.notifyOk(superHeroes)
    }

    override fun onQueryHeroesFailed(callError: CallError) {
        this.notifyFailed(contextRef?.get()?.getString(callError.msgStringRes) ?: "Connection failed")
    }

    private fun notifyOk(superHeroes: List<SuperHero>) {
        listener?.onFetchHeroesOk(superHeroes)
    }

    private fun notifyFailed(reason: String) {
        listener?.onFetchHeroesFailed(reason)
    }
}