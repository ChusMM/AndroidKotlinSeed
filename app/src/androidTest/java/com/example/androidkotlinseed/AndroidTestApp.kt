package com.example.androidkotlinseed

import com.example.androidkotlinseed.injection.AndroidTestApplicationModule
import com.example.androidkotlinseed.injection.AndroidTestUseCaseModule
import com.example.androidkotlinseed.injection.DaggerAndroidTestApplicationComponent
import com.example.androidkotlinseed.injection.application.ApplicationComponent

class AndroidTestApp : App() {
    override fun buidAppComponent(): ApplicationComponent {
        return DaggerAndroidTestApplicationComponent.builder()
            .applicationModule(AndroidTestApplicationModule(this))
            .useCaseModule(AndroidTestUseCaseModule())
            .build()
    }
}