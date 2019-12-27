package com.example.androidkotlinseed.view.mvc.heroeslist

import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.ViewMvc

interface HeroesListViewMvc : ViewMvc, SwipeRefreshLayout.OnRefreshListener {
    var viewListener: ViewListener?
    val heroListObserver: Observer<List<SuperHero>>

    fun onHeroesFetched()
    fun onHeroesFetchFailed(msg: String)

    interface ViewListener {
        fun onSwipeGesture()
        fun onClickHero(superHero: SuperHero)
    }
}