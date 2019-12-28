package com.example.androidkotlinseed.view.mvc.heroeslist

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
import com.example.androidkotlinseed.view.adapters.HeroesAdapter
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import com.example.androidkotlinseed.view.dialogs.ErrorDialogFragment
import kotlinx.android.synthetic.main.activity_heroes_list.view.*

class HeroesListViewMvcImpl(layoutInflater: LayoutInflater,
                            container: ViewGroup?,
                            private val dialogManager: DialogsManager) : HeroesListViewMvc {

    companion object {
        private val TAG = HeroesListViewMvcImpl::class.java.simpleName
    }

    override var rootView: View = layoutInflater.inflate(R.layout.activity_heroes_list, container, false)
    override var viewListener: HeroesListViewMvc.ViewListener? = null
    override val heroListObserver = Observer<List<SuperHero>> { newList ->
        run {
            val heroesAdapter = HeroesAdapter(newList, viewListener, rootView.context)
            rootView.recycler_heroes.adapter = heroesAdapter

            this.onHeroesFetched()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = with(rootView) {
        recycler_heroes.layoutManager = GridLayoutManager(context, 2)
        recycler_heroes.setHasFixedSize(true)

        swipe_layout.setOnRefreshListener(this@HeroesListViewMvcImpl)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = with(rootView) {
        recycler_heroes.visibility = View.GONE
        swipe_layout.isRefreshing = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = with(rootView) {
        swipe_layout.setOnRefreshListener(null)
    }

    override fun onRefresh() {
        this.onStart()
        viewListener?.onSwipeGesture()
    }

    override fun onHeroesFetched() = with(rootView) {
        swipe_layout.isRefreshing = false
        recycler_heroes.visibility = View.VISIBLE
    }

    override fun onHeroesFetchFailed(msg: String) = with(rootView) {
        Log.e(TAG, "Heroes call failed")

        swipe_layout.isRefreshing = false
        recycler_heroes.visibility = View.VISIBLE
        dialogManager.showDialogWithId(ErrorDialogFragment.newInstance(msg), "")
    }
}