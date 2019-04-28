package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.HashSet

class HeroListViewModel(private val fetchHeroesUseCase: FetchHeroesUseCase)
    : ViewModel(), FetchHeroesUseCase.Listener {

    interface Listener {
        fun onHeroesFetched(superHeroes: List<SuperHero>)
        fun onHeroesFetchFailed(msg: String)
    }

    val heroList: MutableLiveData<List<SuperHero>> by lazy {
        MutableLiveData<List<SuperHero>>()
    }
    private val listeners = HashSet<Listener>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchHeroesUseCase.registerListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        fetchHeroesUseCase.unregisterListener(this)
        compositeDisposable.clear()
    }

    fun fetchHeroesAndNotify() {
        fetchHeroesUseCase.fetchAndNotify()
    }

    override fun onFetchHeroesOk(superHeroes: List<SuperHero>) {
        heroList.value = superHeroes
        val disposable = Flowable.fromIterable(listeners)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listener -> listener.onHeroesFetched(superHeroes) }
        compositeDisposable.add(disposable)
    }

    override fun onFetchHeroesFailed(msg: String) {
        val disposable = Flowable.fromIterable(listeners)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listener -> listener.onHeroesFetchFailed(msg) }
        compositeDisposable.add(disposable)
    }
}