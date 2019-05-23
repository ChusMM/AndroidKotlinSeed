package com.example.androidkotlinseed.injection.presentation

import com.example.androidkotlinseed.utils.ImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BindingsModule {
    @Singleton
    @Provides
    fun getImageLoader(): ImageLoader = ImageLoader()
}