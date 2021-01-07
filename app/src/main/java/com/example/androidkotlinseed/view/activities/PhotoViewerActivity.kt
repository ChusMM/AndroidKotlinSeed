package com.example.androidkotlinseed.view.activities

import android.app.Activity
import android.os.Bundle
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import com.example.androidkotlinseed.view.mvc.photoviewer.PhotoViewerMvc
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotoViewerActivity : BaseActivity(), PhotoViewerMvc.ViewListener {
    companion object {
        const val EXTRA_IMAGE_URL = "image_url"
    }

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: PhotoViewerMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newInstance(PhotoViewerMvc::class, null)
        viewMvc.viewListener = this

        setContentView(viewMvc.rootView)

        intent.getStringExtra(EXTRA_IMAGE_URL)?.let { imageUrl ->
            viewMvc.loadImage(imageUrl)
        }
    }

    override fun onWriteExternalStorageResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            viewMvc.shareImage()
        }
    }

    override fun onCloseClicked() {
        this.finish()
    }

    override fun onShareClicked() {
        if (super.hasWriteStoragePermission()) {
            viewMvc.shareImage()
        } else {
            super.requestWriteStoragePermission()
        }
    }
}
