package com.example.androidkotlinseed.view.mvc.photoviewer

import com.example.androidkotlinseed.view.mvc.ViewMvc

interface PhotoViewerMvc : ViewMvc {
    var viewListener: ViewListener?

    fun loadImage(imageUrl: String)
    fun shareImage()

    interface ViewListener {
        fun onCloseClicked()
        fun onShareClicked()
    }
}