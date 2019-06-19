package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.*
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HeroListViewModel @Inject constructor (private val fetchHeroesUseCase: FetchHeroesUseCase)
    : ViewModel(), LifecycleObserver, FetchHeroesUseCase.Listener {

    val heroList: MutableLiveData<List<SuperHero>> by lazy {
        MutableLiveData<List<SuperHero>>()
    }
    private val listeners: MutableSet<HeroesListViewMvc> = mutableSetOf()
    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerUsecaseListener() {
        fetchHeroesUseCase.registerListener(this)
        this.fetchHeroesAndNotify()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterUsecaseListener() {
        fetchHeroesUseCase.unregisterListener(this)
        compositeDisposable.dispose()
    }

    fun registerListener(listener: HeroesListViewMvc) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: HeroesListViewMvc) {
        listeners.remove(listener)
    }

    private fun fetchHeroesAndNotify() {
        val refresh = heroList.value?.isEmpty() ?: true
        this.fetchHeroesAndNotify(refresh)
    }

    private fun fetchHeroesAndNotify(forceRefresh: Boolean) {
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