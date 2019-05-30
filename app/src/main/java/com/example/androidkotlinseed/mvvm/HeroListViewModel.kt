package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HeroListViewModel @Inject constructor (private val fetchHeroesUseCase: FetchHeroesUseCase)
    : ViewModel(), FetchHeroesUseCase.Listener {

    interface Listener {
        fun onHeroesFetched(superHeroes: List<SuperHero>)
        fun onHeroesFetchFailed(msg: String)
    }

    val heroList: MutableLiveData<List<SuperHero>> by lazy {
        MutableLiveData<List<SuperHero>>()
    }
    private val listeners: MutableSet<Listener> = mutableSetOf()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchHeroesUseCase.registerListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        fetchHeroesUseCase.unregisterListener(this)
        compositeDisposable.dispose()
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun fetchHeroesAndNotify() {
        val refresh = heroList.value?.isEmpty() ?: true
        this.fetchHeroesAndNotify(refresh)
    }

    fun fetchHeroesAndNotify(forceRefresh: Boolean) {
        if (forceRefresh) {
            fetchHeroesUseCase.fetchAndNotify()
        } else {
            heroList.postValue(heroList.value)
            heroList.value?.let { onFetchHeroesOk(it) }
        }
    }

    fun clearList() {
        heroList.value = mutableListOf()
    }

    override fun onFetchHeroesOk(superHeroes: List<SuperHero>) {
        heroList.value = superHeroes

        val disposable = Flowable.fromIterable(listeners)
            .subscribe { listener -> listener.onHeroesFetched(superHeroes) }
        compositeDisposable.add(disposable)
    }

    override fun onFetchHeroesFailed(msg: String) {
        val disposable = Flowable.fromIterable(listeners)
            .subscribe { listener -> listener.onHeroesFetchFailed(msg) }
        compositeDisposable.add(disposable)
    }
}