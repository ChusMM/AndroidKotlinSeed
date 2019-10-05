package com.example.androidkotlinseed.view.mvc.photoviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.utils.ImageLoader
import com.example.androidkotlinseed.utils.ImageUtils
import android.graphics.drawable.BitmapDrawable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.photo_viewer.view.*
import java.util.*


class PhotoViewerViewMvcImpl(layoutInflater: LayoutInflater,
                             container: ViewGroup?,
                             private val imageUtils: ImageUtils,
                             private val imageLoader: ImageLoader): PhotoViewerMvc,
    ImageLoader.LoadFinishListener {

    override var rootView: View = layoutInflater.inflate(R.layout.photo_viewer, container, false)
    override var viewListener: PhotoViewerMvc.ViewListener? = null

    private var disposable: Disposable? = null

    init {
        this.initViews()
    }

    private fun initViews() = with(rootView) {
        close_view.setOnClickListener {
            disposable?.dispose()
            viewListener?.onCloseClicked()
        }

        share_image.setOnClickListener {
            viewListener?.onShareClicked()
        }
    }

    override fun loadImage(imageUrl: String) = with(rootView) {
        progress_circular.visibility = View.VISIBLE
        imageLoader.loadFromUrl(imageUrl, image_to_show, this@PhotoViewerViewMvcImpl)
    }

    override fun shareImage() = with(rootView) {
        val bitmap = (image_to_show.drawable as? BitmapDrawable)?.bitmap

        bitmap?.let {
            val imageName = "AndroidSeed" + Date().time + ".png"

            disposable?.dispose()
            disposable = imageUtils.shareImageVia(it, imageName)
        }
        return@with
    }

    override fun onImageLoadedSuccessfull()  = with(rootView) {
        progress_circular.visibility = View.GONE
    }

    override fun onImageLoadedError() = with(rootView) {
        progress_circular.visibility = View.GONE
    }
}