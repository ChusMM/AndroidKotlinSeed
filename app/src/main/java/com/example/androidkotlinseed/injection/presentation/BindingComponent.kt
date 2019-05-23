package com.example.androidkotlinseed.injection.presentation

import com.example.androidkotlinseed.view.adapters.HeroBindingAdapter
import dagger.Subcomponent

@Subcomponent(modules = [BindingsModule::class])
interface BindingComponent {
    fun inject(heroBindingAdapter: HeroBindingAdapter)
}