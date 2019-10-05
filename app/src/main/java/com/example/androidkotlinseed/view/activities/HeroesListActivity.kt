package com.example.androidkotlinseed.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.*
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.mvvm.*
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory

import javax.inject.Inject

class HeroesListActivity : BaseActivity(), LifecycleOwner, HeroesListViewMvc.ViewListener {
    @Inject lateinit var viewMvcFactory: ViewMvcFactory
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var lifecycleRegistry: LifecycleRegistry

    private lateinit var heroListViewModel: HeroListViewModel
    private lateinit var viewMvc: HeroesListViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresentationComponent().inject(this)

        viewMvc = viewMvcFactory.newInstance(HeroesListViewMvc::class, null)
        viewMvc.viewListener = this

        setContentView(viewMvc.rootView)

        heroListViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(HeroListViewModel::class.java)
        heroListViewModel.heroList.observe(this, viewMvc.heroListObserver)

        lifecycleRegistry.addObserver(heroListViewModel)
        lifecycleRegistry.addObserver(viewMvc)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        heroListViewModel.registerViewBinder(viewMvc)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        super.onStop()
        heroListViewModel.unregisterViewBinder(viewMvc)
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
