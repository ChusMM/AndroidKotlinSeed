package com.example.androidkotlinseed.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.mvvm.HeroDetailViewModel
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvc
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HeroDetailActivity : BaseActivity(), HeroDetailViewMvc.ViewListener {
    companion object {
        const val HERO_EXTRA = "hero_extra"
    }

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private val heroDetailViewModel: HeroDetailViewModel by viewModels()
    private lateinit var viewMvc: HeroDetailViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newInstance(HeroDetailViewMvc::class, null)
        viewMvc.viewListener = this

        setContentView(viewMvc.rootView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        heroDetailViewModel.registerViewBinder(viewMvc)
        heroDetailViewModel.heroBound.observe(this, viewMvc.heroDetailObserver)

        val hero = intent.getParcelableExtra(HERO_EXTRA) as? SuperHero
        heroDetailViewModel.bindHero(hero)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onZoomClicked(hero: SuperHero) {
        val intent = Intent(this, PhotoViewerActivity::class.java)
        intent.putExtra(PhotoViewerActivity.EXTRA_IMAGE_URL, hero.photo)

        startActivity(intent)
    }
}
