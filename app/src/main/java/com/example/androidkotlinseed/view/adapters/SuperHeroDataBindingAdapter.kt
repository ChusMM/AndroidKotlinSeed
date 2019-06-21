package com.example.androidkotlinseed.view.adapters

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.view.adapters.SuperHeroDataBindingAdapter.Companion.TAG
import java.lang.RuntimeException

class SuperHeroDataBindingAdapter(private val imageLoader: ImageLoader) {
    companion object {
        val TAG: String = SuperHeroDataBindingAdapter::class.java.simpleName
    }

    init {
        filePrivateImageLoader = this.imageLoader
    }
}


private var filePrivateImageLoader: ImageLoader? = null

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    filePrivateImageLoader?.loadFromUrlFor43AspectRatio(imageUrl, view, R.drawable.placeholder) ?: run {
        Log.e(TAG, "Image loader not instanced")
        throw RuntimeException("Image loader for bingding adaper not instanced")
    }
}