package com.example.androidkotlinseed.view.mvc.herodetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                            private val dialogsManager: DialogsManager,
                            private val imageLoader: ImageLoader):
    HeroDetailViewMvc, ImageLoader.LoadFinishListener, View.OnClickListener {

    override var viewListener: HeroDetailViewMvc.ViewListener? = null
    private lateinit var heroBound: SuperHero

    companion object {
        private val TAG = HeroDetailViewMvcImpl::class.java.simpleName
    }

    override var rootView: View = layoutInflater.inflate(R.layout.activity_hero_detail, container, false)

    override fun bindHero(hero: SuperHero?) = with(rootView) {
        hero?.let {
            this@HeroDetailViewMvcImpl.heroBound = it

            imageLoader.loadFromUrl(it.photo, rootView.hero_pic, this@HeroDetailViewMvcImpl)

            val attrAdapter = HeroAttributesAdapter(it, context)
            hero_attrs_list.layoutManager = LinearLayoutManager(context)
            hero_attrs_list.adapter = attrAdapter

            image_detail_button.setOnClickListener(this@HeroDetailViewMvcImpl)

        } ?: run {
            Log.e(TAG, "No hero provided to show its detail")
            dialogsManager.showDialogWithId(ErrorDialogFragment.newInstance(context.getString(R.string.hero_not_bound)), "")
        }
    }

    override fun onImageLoaded() = with(rootView) {
        image_detail_button.visibility = View.VISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.image_detail_button -> viewListener?.onZoomClicked(heroBound)
        }
    }
}