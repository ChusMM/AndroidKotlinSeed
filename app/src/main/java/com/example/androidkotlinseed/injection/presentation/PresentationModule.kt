package com.example.androidkotlinseed.injection.presentation

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleRegistry
import com.example.androidkotlinseed.utils.AppRxSchedulers
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.utils.ImageUtils
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class PresentationModule {
    @Provides
    fun context(activity: FragmentActivity): Context = activity

    @Provides
    fun getFragmentManager(activity: FragmentActivity): FragmentManager = activity.supportFragmentManager

    @Provides
    fun getLayoutInflater(activity: Activity): LayoutInflater = LayoutInflater.from(activity)

    @Provides
    fun getDialogManager(fragmentManager: FragmentManager): DialogsManager = DialogsManager(fragmentManager)

    @Provides
    fun getLifecycleRegistry(activity: FragmentActivity): LifecycleRegistry = LifecycleRegistry(activity)

    @Provides
    fun getImageUtils(activity: Activity, appRxSchedulers: AppRxSchedulers): ImageUtils =
            ImageUtils(activity, appRxSchedulers)

    @Reusable
    @Provides
    fun getImageLoader(): ImageLoader = ImageLoader()

    @Provides
    fun getViewMvcFactory(layoutInflater: LayoutInflater,
                          dialogsManager: DialogsManager,
                          lifecycleRegistry: LifecycleRegistry,
                          imageUtils: ImageUtils,
                          imageLoader: ImageLoader): ViewMvcFactory =
            ViewMvcFactory(layoutInflater, dialogsManager, lifecycleRegistry, imageUtils, imageLoader)
}