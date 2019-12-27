package com.example.androidkotlinseed.view.mvc.herodetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.view.adapters.HeroAttributesAdapter
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import com.example.androidkotlinseed.view.dialogs.ErrorDialogFragment
import kotlinx.android.synthetic.main.activity_hero_detail.view.*

class HeroDetailViewMvcImpl(layoutInflater: LayoutInflater,
                            container: ViewGroup?,
                            private val dialogManager: DialogsManager,
                            private val imageLoader: ImageLoader) : HeroDetailViewMvc {

    companion object {
        private val TAG = HeroDetailViewMvcImpl::class.java.simpleName
    }

    override var rootView: View = layoutInflater.inflate(R.layout.activity_hero_detail, container, false)
    override var viewListener: HeroDetailViewMvc.ViewListener? = null
    override val heroDetailObserver = Observer<SuperHero> { superHero ->
        bindHero(superHero)
    }

    private fun bindHero(hero: SuperHero?) = with(rootView) {
        hero?.let { superHero ->
            imageLoader.loadFromUrl(superHero.photo, rootView.hero_pic)

            val attrAdapter = HeroAttributesAdapter(superHero, context)
            hero_attrs_list.layoutManager = LinearLayoutManager(context)
            hero_attrs_list.adapter = attrAdapter

            image_detail_button.setOnClickListener {
                viewListener?.onZoomClicked(superHero)
            }
        } ?: run {
            Log.e(TAG, "No hero provided to show its detail")
            dialogManager.showDialogWithId(ErrorDialogFragment.newInstance(context.getString(R.string.hero_not_bound)), "")
        }
    }
}