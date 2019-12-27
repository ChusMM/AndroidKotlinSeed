package com.example.androidkotlinseed.view.mvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.utils.ImageUtils
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvc
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvcImpl
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvcImpl
import com.example.androidkotlinseed.view.mvc.photoviewer.PhotoViewerMvc
import com.example.androidkotlinseed.view.mvc.photoviewer.PhotoViewerViewMvcImpl
import kotlin.reflect.KClass

class ViewMvcFactory(private val layoutInflater: LayoutInflater,
                     private val dialogsManager: DialogsManager,
                     private val imageUtils: ImageUtils,
                     private val imageLoader: ImageLoader) {

    fun <T : ViewMvc> newInstance(mvcViewClass: KClass<T>, container: ViewGroup?): T {
        @Suppress("UNCHECKED_CAST")
        (return when (mvcViewClass) {
            HeroesListViewMvc::class -> HeroesListViewMvcImpl(layoutInflater, container, dialogsManager) as T
            HeroDetailViewMvc::class -> HeroDetailViewMvcImpl(layoutInflater, container, dialogsManager, imageLoader) as T
            PhotoViewerMvc::class    -> PhotoViewerViewMvcImpl(layoutInflater, container, imageUtils, imageLoader) as T
            else                     -> throw IllegalArgumentException("unsupported MVC view class $mvcViewClass")
        })
    }
}