package com.example.androidkotlinseed.view.adapters

import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.injection.BaseBindingAdapter
import com.example.androidkotlinseed.utils.ImageLoader
import javax.inject.Inject

class HeroBindingAdapter(application: Application) : BaseBindingAdapter(application) {
    @Inject
    lateinit var imageLoader: ImageLoader

    init {
        super.getBindingComponent().inject(this)
        filePrivateImageLoader = this.imageLoader
    }
}

private var filePrivateImageLoader: ImageLoader? = null

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    filePrivateImageLoader?.loadFromUrlFor43AspectRatio(imageUrl, view, R.drawable.placeholder)
}