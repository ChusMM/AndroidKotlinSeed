package com.example.androidkotlinseed.view.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.view.adapters.HeroAttributesAdapter
import com.example.androidkotlinseed.view.dialogs.CallErrorDialogFragment
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import kotlinx.android.synthetic.main.activity_hero_detail.hero_attrs_list as heroAttrs
import kotlinx.android.synthetic.main.activity_hero_detail.hero_pic as heroPic
import javax.inject.Inject

class HeroDetailActivity : BaseActivity() {
    private val TAG = HeroDetailActivity::class.java.simpleName

    companion object {
        const val HERO_EXTRA = "hero_extra"
    }

    @Inject lateinit var dialogsManager: DialogsManager
    @Inject lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_detail)
        super.getPresentationComponent().inject(this)

        val hero = intent.getParcelableExtra(HERO_EXTRA) as SuperHero?
        this.bindHero(hero)
    }

    private fun bindHero(hero: SuperHero?) {
        hero?.let {
            imageLoader.loadFromUrl(it.photo, heroPic)

            val attrAdapter = HeroAttributesAdapter(it, this)
            heroAttrs.layoutManager = LinearLayoutManager(this)
            heroAttrs.adapter = attrAdapter
        } ?: run {
            Log.e(TAG, "No hero provided to show its detail")
            dialogsManager.showDialogWithId(CallErrorDialogFragment.newInstance(), "")
        }
    }
}
