package com.example.androidkotlinseed.view.activities

import android.os.Bundle
import androidx.lifecycle.*
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.mvvm.*
import com.example.androidkotlinseed.view.adapters.SuperHeroDataBindingAdapter
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory

import javax.inject.Inject

class HeroesListActivity : BaseActivity(), LifecycleOwner {

    @Inject lateinit var viewMvcFactory: ViewMvcFactory
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var heroListViewModel: HeroListViewModel
    @Inject lateinit var lifecycleRegistry: LifecycleRegistry
    @Suppress("unused")
    @Inject lateinit var superHeroDataBindingAdapter: SuperHeroDataBindingAdapter

    private lateinit var viewMvc: HeroesListViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresentationComponent().inject(this)

        viewMvc = viewMvcFactory.newInstance(HeroesListViewMvc::class, null)
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
}
