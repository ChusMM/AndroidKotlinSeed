package com.example.androidkotlinseed

import android.app.Application
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.injection.application.DaggerApplicationComponent
import com.example.androidkotlinseed.injection.application.UseCaseModule
import java.lang.IllegalStateException

class App : Application() {
    private lateinit var appComponent: ApplicationComponent

    companion object {
        private var appInstance: App? = null

        fun getAppInstance(): App {
            appInstance?.let {
                return it
            } ?: throw IllegalStateException("App not instanced")
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        appComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .useCaseModule(UseCaseModule())
            .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return appComponent
    }
}
