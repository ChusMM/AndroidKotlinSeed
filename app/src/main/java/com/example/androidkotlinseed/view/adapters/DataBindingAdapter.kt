package com.example.androidkotlinseed.view.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.utils.ImageLoader

const val TAG: String = "DataBindingAdapter"

private var filePrivateImageLoader = ImageLoader()

@BindingAdapter("heroImageUrl")
fun loadHeroImage(view: ImageView, imageUrl: String) {
    filePrivateImageLoader.loadFromUrlFor43AspectRatio(imageUrl, view, R.drawable.placeholder)
}