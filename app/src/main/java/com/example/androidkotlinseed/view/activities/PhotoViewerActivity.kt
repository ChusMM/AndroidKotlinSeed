package com.example.androidkotlinseed.view.activities

import android.Manifest
import android.app.Activity
import android.os.Bundle
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.view.mvc.ViewMvcFactory
import com.example.androidkotlinseed.view.mvc.photoviewer.PhotoViewerMvc
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class PhotoViewerActivity : BaseActivity(), PhotoViewerMvc.ViewListener {
    companion object {
        const val EXTRA_IMAGE_URL = "image_url"
    }

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: PhotoViewerMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresentationComponent().inject(this)

        viewMvc = viewMvcFactory.newInstance(PhotoViewerMvc::class, null)
        viewMvc.viewListener = this

        setContentView(viewMvc.rootView)

        intent.getStringExtra(EXTRA_IMAGE_URL).let { imageUrl ->
            viewMvc.loadImage(imageUrl)
        }
    }

    override fun onRequestWriteExternalStorage(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            viewMvc.shareImage()
        }
    }

    override fun onCloseClicked() {
        this.finish()
    }

    override fun onShareClicked() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            viewMvc.shareImage()
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.request_write_storage),
                REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}
