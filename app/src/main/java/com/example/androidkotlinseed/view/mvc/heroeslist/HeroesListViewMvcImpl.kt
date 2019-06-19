package com.example.androidkotlinseed.view.mvc.heroeslist

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.activities.HeroDetailActivity
import com.example.androidkotlinseed.view.adapters.HeroesAdapter
import com.example.androidkotlinseed.view.dialogs.ErrorDialogFragment
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import kotlinx.android.synthetic.main.activity_heroes_list.view.*

class HeroesListViewMvcImpl(layoutInflater: LayoutInflater,
                            container: ViewGroup?,
                            private val dialogsManager: DialogsManager) :
    HeroesListViewMvc {

    companion object {
        private val TAG = HeroesListViewMvcImpl::class.java.simpleName
    }

    override var rootView: View = layoutInflater.inflate(R.layout.activity_heroes_list, container, false)

    init {
        this.initViews()
    }

    private fun initViews() = with(rootView) {
        recycler_heroes.layoutManager = GridLayoutManager(context, 2)
        recycler_heroes.setHasFixedSize(true)
    }

    private val heroListObserver = Observer<List<SuperHero>> { newList ->
        run {
            val heroesAdapter = HeroesAdapter(newList, this, rootView.context)
            rootView.recycler_heroes.adapter = heroesAdapter

            this.onHeroesFetched()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = with(rootView) {
        swipe_layout.setOnRefreshListener(this@HeroesListViewMvcImpl)
        this@HeroesListViewMvcImpl.onRefresh()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()  = with(rootView) {
        swipe_layout.setOnRefreshListener(null)
    }

    override fun getViewModelObserver(): Observer<List<SuperHero>> {
        return heroListObserver
    }

    override fun onRefresh() = with(rootView) {
        recycler_heroes.visibility = View.GONE
        swipe_layout.isRefreshing = true
    }

    override fun onHeroesFetched() = with(rootView) {
        swipe_layout.isRefreshing = false
        recycler_heroes.visibility = View.VISIBLE
    }

    override fun onHeroesFetchFailed(msg: String) = with(rootView) {
        Log.e(TAG, "Heroes call failed")

        swipe_layout.isRefreshing = false
        recycler_heroes.visibility = View.VISIBLE
        dialogsManager.showDialogWithId(ErrorDialogFragment.newInstance(), "")
    }

    override fun onClickHero(superHero: SuperHero) = with(rootView) {
        val intent = Intent(context, HeroDetailActivity::class.java)
        intent.putExtra(HeroDetailActivity.HERO_EXTRA, superHero)

        context.startActivity(intent)
    }
}