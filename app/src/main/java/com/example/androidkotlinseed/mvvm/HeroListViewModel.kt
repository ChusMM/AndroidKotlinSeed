package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.*
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.view.mvc.IViewBinder
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HeroListViewModel @Inject constructor (private val fetchHeroesUseCase: IFetchHeroesUseCase)
    : ViewModel(), LifecycleObserver, IFetchHeroesUseCase.Listener, IViewBinder<HeroesListViewMvc> {

    val heroList: MutableLiveData<List<SuperHero>> by lazy {
        @Suppress("RemoveExplicitTypeArguments")
        MutableLiveData<List<SuperHero>>()
    }
    override val viewBinders: MutableSet<HeroesListViewMvc> = mutableSetOf()

    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerUsecaseListener() {
        fetchHeroesUseCase.registerListener(this)
        this.fetchHeroesAndNotify()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterUsecaseListener() {
        fetchHeroesUseCase.unregisterListener(this)
        fetchHeroesUseCase.dispose()
        compositeDisposable.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clearList() {
        heroList.value = mutableListOf()
    }

    private fun fetchHeroesAndNotify() {
        val refresh = heroList.value?.isEmpty() ?: true
        this.fetchHeroesAndNotify(refresh)
    }

    fun fetchHeroesAndNotify(forceRefresh: Boolean) {
        if (forceRefresh) {
            fetchHeroesUseCase.fetchAndNotify()
        } else {
            heroList.postValue(heroList.value)
        }
    }

    //region Usecase Listener
    override fun onFetchHeroesOk(superHeroes: List<SuperHero>) {
        heroList.value = superHeroes
    }

    override fun onFetchHeroesFailed(msg: String) {
        val disposable = Flowable.fromIterable(viewBinders)
            .subscribe { viewBinder -> viewBinder.onHeroesFetchFailed(msg) }
        compositeDisposable.add(disposable)
    }
    //endregion
}