package com.example.androidkotlinseed.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.mvvm.HeroDetailViewModel
import com.example.androidkotlinseed.mvvm.HeroListViewModel
import com.example.androidkotlinseed.mvvm.ViewModelFactory
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvc
import javax.inject.Inject

class HeroDetailActivity : BaseActivity(), HeroDetailViewMvc.ViewListener {
    companion object {
        const val HERO_EXTRA = "hero_extra"
    }

    @Inject lateinit var viewMvcFactory: ViewMvcFactory
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var heroDetailViewModel: HeroDetailViewModel
    private lateinit var viewMvc: HeroDetailViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.getPresentationComponent().inject(this)

        viewMvc = viewMvcFactory.newInstance(HeroDetailViewMvc::class, null)
        viewMvc.viewListener = this

        setContentView(viewMvc.rootView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        heroDetailViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(HeroDetailViewModel::class.java)
        heroDetailViewModel.registerViewBinder(viewMvc)
        heroDetailViewModel.heroBound.observe(this, viewMvc.heroDetailObserver)

        val hero = intent.getParcelableExtra(HERO_EXTRA) as? SuperHero
        heroDetailViewModel.bindHero(hero)
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
