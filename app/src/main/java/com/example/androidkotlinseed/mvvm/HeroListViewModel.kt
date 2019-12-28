package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.view.mvc.IViewBinder
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HeroListViewModel @Inject constructor(private val fetchHeroesUseCase: IFetchHeroesUseCase)
    : ViewModel(), LifecycleObserver, IFetchHeroesUseCase.Listener, IViewBinder<HeroesListViewMvc> {

    var forceRefresh: Boolean = false

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
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clearList() {
        heroList.value = mutableListOf()
    }

    fun fetchHeroesAndNotify() {
        val mayRefresh = heroList.value?.isEmpty() ?: true
        this.forceRefresh = this.forceRefresh || mayRefresh

        if (forceRefresh) {
            fetchHeroesUseCase.fetchAndNotify()
            this.forceRefresh = false
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