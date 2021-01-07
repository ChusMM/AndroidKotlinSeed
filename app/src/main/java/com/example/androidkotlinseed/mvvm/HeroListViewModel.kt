package com.example.androidkotlinseed.mvvm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.view.mvc.IViewBinder
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

class HeroListViewModel @ViewModelInject constructor(
    private val fetchHeroesUseCase: IFetchHeroesUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver, IFetchHeroesUseCase.Listener, IViewBinder<HeroesListViewMvc> {

    var forceRefresh: Boolean = false

    val heroList: LiveData<List<SuperHero>> get() = _heroList
    private val _heroList: MutableLiveData<List<SuperHero>> by lazy {
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
        _heroList.value = mutableListOf()
    }

    fun fetchHeroesAndNotify() {
        val mayRefresh = heroList.value?.isEmpty() ?: true
        this.forceRefresh = this.forceRefresh || mayRefresh

        if (forceRefresh) {
            fetchHeroesUseCase.fetchAndNotify()
            this.forceRefresh = false
        } else {
            _heroList.postValue(heroList.value)
        }
    }

    //region Usecase Listener
    override fun onFetchHeroesOk(superHeroes: List<SuperHero>) {
        _heroList.value = superHeroes
    }

    override fun onFetchHeroesFailed(msg: String) {
        val disposable = Flowable.fromIterable(viewBinders)
                .subscribe { viewBinder -> viewBinder.onHeroesFetchFailed(msg) }
        compositeDisposable.add(disposable)
    }
    //endregion
}