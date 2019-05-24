package com.example.androidkotlinseed.view.adapters

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.view.adapters.HeroBindingAdapter.Companion.TAG

class HeroBindingAdapter(private val imageLoader: ImageLoader) {
    companion object {
        val TAG: String = HeroBindingAdapter::class.java.simpleName
    }

    init {
        filePrivateImageLoader = this.imageLoader
    }
}


private var filePrivateImageLoader: ImageLoader? = null

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    filePrivateImageLoader?.loadFromUrlFor43AspectRatio(imageUrl, view, R.drawable.placeholder)
        ?: Log.e(TAG, "Image loader not instanced")
}