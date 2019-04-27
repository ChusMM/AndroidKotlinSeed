package com.example.androidkotlinseed.view.activities

import android.os.Bundle
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import javax.inject.Inject

class HeroesListActivity : BaseActivity() {
    private val TAG = HeroesListActivity::class.simpleName

    @Inject
    lateinit var fetchHeroesUseCase: FetchHeroesUseCase

    @Inject
    lateinit var dialogsManager: DialogsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heroes_list)

        getPresentationComponent().inject(this)
    }
}
