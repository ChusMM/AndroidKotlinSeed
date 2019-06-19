package com.example.androidkotlinseed.view.mvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.utils.ImageUtils
import com.example.androidkotlinseed.view.dialogs.DialogsManager
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroListViewMvc
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvcImpl
import kotlin.reflect.KClass

class ViewMvcFactory(private val layoutInflater: LayoutInflater,
                     private val dialogsManager: DialogsManager,
                     private val imageUtils: ImageUtils,
                     private val imageLoader: ImageLoader) {

    fun <T : ViewMvc> newInstance(mvcViewClass: KClass<T>, container: ViewGroup?): T {
        val viewMvc: ViewMvc
        if (mvcViewClass == HeroListViewMvc::class) {
            viewMvc =
                HeroesListViewMvcImpl(
                    layoutInflater,
                    container,
                    dialogsManager
                )
        } else {
            throw IllegalArgumentException("unsupported MVC view class $mvcViewClass")
        }

        @Suppress("UNCHECKED_CAST")
        return viewMvc as T
    }
}