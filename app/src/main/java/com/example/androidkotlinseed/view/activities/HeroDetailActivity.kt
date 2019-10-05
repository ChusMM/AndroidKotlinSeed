package com.example.androidkotlinseed.view.activities

import android.os.Bundle
import android.view.MenuItem
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvc
import javax.inject.Inject

class HeroDetailActivity : BaseActivity(), HeroDetailViewMvc.ViewListener {
    companion object {
        const val HERO_EXTRA = "hero_extra"
    }

    @Inject lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: HeroDetailViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.getPresentationComponent().inject(this)

        viewMvc = viewMvcFactory.newInstance(HeroDetailViewMvc::class, null)
        viewMvc.viewListener = this
        setContentView(viewMvc.rootView)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val hero = intent.getParcelableExtra(HERO_EXTRA) as? SuperHero
        viewMvc.bindHero(hero)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onZoomClicked(hero: SuperHero) {

    }
}
