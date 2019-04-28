package com.example.androidkotlinseed.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.mvvm.HeroListViewModel
import com.example.androidkotlinseed.mvvm.ViewModelFactory
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.view.adapters.HeroesAdapter
import com.example.androidkotlinseed.view.dialogs.CallErrorDialogFragment
import com.example.androidkotlinseed.view.dialogs.DialogsManager

import kotlinx.android.synthetic.main.activity_heroes_list.recycler_heroes as recyclerHeroes
import kotlinx.android.synthetic.main.activity_heroes_list.swipe_layout as swipeRefreshLayout

import javax.inject.Inject

class HeroesListActivity : BaseActivity(), HeroListViewModel.Listener, SwipeRefreshLayout.OnRefreshListener {

    private val TAG = HeroesListActivity::class.simpleName

    @Inject lateinit var dialogsManager: DialogsManager
    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var heroListViewModel: HeroListViewModel

    private val heroObserver = Observer<List<SuperHero>> { newList -> run {
        val heroesAdapter = HeroesAdapter(newList, this, imageLoader)
        recyclerHeroes.adapter = heroesAdapter }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heroes_list)
        getPresentationComponent().inject(this)

        heroListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HeroListViewModel::class.java)
        heroListViewModel.heroList.observe(this, heroObserver)

        recyclerHeroes.layoutManager = GridLayoutManager(this, 2)
        recyclerHeroes.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        swipeRefreshLayout.setOnRefreshListener(this)

        recyclerHeroes.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = true
    }

    override fun onRefresh() {
        heroListViewModel.fetchHeroesAndNotify()
    }

    override fun onHeroesFetched(superHeroes: List<SuperHero>) {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun onHeroesFetchFailed(msg: String) {
        Log.e(TAG, "Heroes call failed")
        dialogsManager.showDialogWithId(CallErrorDialogFragment.newInstance(), "")
    }
}
