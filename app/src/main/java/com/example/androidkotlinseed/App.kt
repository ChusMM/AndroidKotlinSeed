package com.example.androidkotlinseed

import android.app.Application
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.injection.application.DaggerApplicationComponent
import com.example.androidkotlinseed.injection.application.UseCaseModule

class App : Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .useCaseModule(UseCaseModule())
            .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return appComponent
    }
}
