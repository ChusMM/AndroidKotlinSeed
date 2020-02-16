package com.example.androidkotlinseed

import android.annotation.SuppressLint
import android.app.Application
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.injection.application.DaggerApplicationComponent
import com.example.androidkotlinseed.injection.application.UseCaseModule

@SuppressLint("Registered")
open class App : Application() {
    private lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = this.buidAppComponent()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return appComponent
    }

    protected open fun buidAppComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .useCaseModule(UseCaseModule())
                .build()
    }
}
