package com.example.androidkotlinseed.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.*
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.mvvm.*
import com.example.androidkotlinseed.view.adapters.SuperHeroDataBindingAdapter
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory

import javax.inject.Inject

class HeroesListActivity : BaseActivity(), LifecycleOwner, HeroesListViewMvc.ViewListener {
    @Inject lateinit var viewMvcFactory: ViewMvcFactory
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var heroListViewModel: HeroListViewModel
    @Inject lateinit var lifecycleRegistry: LifecycleRegistry
    @Inject lateinit var superHeroDataBindingAdapter: SuperHeroDataBindingAdapter

    private lateinit var viewMvc: HeroesListViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresentationComponent().inject(this)

        viewMvc = viewMvcFactory.newInstance(HeroesListViewMvc::class, null)
        viewMvc.viewListener = this

        setContentView(viewMvc.rootView)

        heroListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroListViewModel::class.java)
        heroListViewModel.heroList.observe(this, viewMvc.getViewModelObserver())

        lifecycleRegistry.addObserver(heroListViewModel)
        lifecycleRegistry.addObserver(viewMvc)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        heroListViewModel.registerListener(viewMvc)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        super.onStop()
        heroListViewModel.unregisterListener(viewMvc)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onSwipeGesture() {
        heroListViewModel.fetchHeroesAndNotify(true)
    }

    override fun onClickHero(superHero: SuperHero) {
        val intent = Intent(this, HeroDetailActivity::class.java)
        intent.putExtra(HeroDetailActivity.HERO_EXTRA, superHero)

        this.startActivity(intent)
    }
}
