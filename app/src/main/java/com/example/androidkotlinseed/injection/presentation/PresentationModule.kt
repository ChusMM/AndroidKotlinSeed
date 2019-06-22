package com.example.androidkotlinseed.injection.presentation

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleRegistry
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.utils.ImageUtils
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class PresentationModule(private val activity: FragmentActivity) {

    @Provides
    fun getFragmentManager(): FragmentManager = activity.supportFragmentManager

    @Provides
    fun getLayoutInflater(): LayoutInflater = LayoutInflater.from(activity)

    @Provides
    fun getActivity(): Activity = activity

    @Provides
    fun context(activity: Activity): Context = activity

    @Provides
    fun getDialogManager(fragmentManager: FragmentManager): DialogsManager {
        return DialogsManager(fragmentManager)
    }

    @Provides
    fun getLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(activity)

    @Provides
    fun getImageUtils(activity: Activity): ImageUtils = ImageUtils(activity)

    @Reusable
    @Provides
    fun getImageLoader(): ImageLoader = ImageLoader()

    @Provides
    fun getViewMvcFactory(layoutInflater: LayoutInflater,
                          dialogsManager: DialogsManager,
                          imageUtils: ImageUtils,
                          imageLoader: ImageLoader): ViewMvcFactory =
        ViewMvcFactory(layoutInflater, dialogsManager, imageUtils, imageLoader)
}