package com.example.androidkotlinseed.injection.application

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    
    @Singleton
    @Provides
    fun getApplication(): Application {
        return application
    }

    @Provides
    fun getAppContext(application: Application): Context {
        return application.applicationContext
    }
}