package com.example.androidkotlinseed.injection

import android.Manifest
import android.content.Intent
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinseed.App
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationModule
import pub.devrel.easypermissions.EasyPermissions


abstract class BaseActivity : AppCompatActivity(),
    EasyPermissions.PermissionCallbacks {
    private var isInjectorUsed = false

    companion object {
        const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1001
    }

    @UiThread
    fun getPresentationComponent(): PresentationComponent {
        if (isInjectorUsed) {
            throw RuntimeException("There is no need to use injector more than once")
        }
        isInjectorUsed = true
        return getApplicationComponent().newPresentationComponent(PresentationModule(this))
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (application as App).getApplicationComponent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE -> onWriteExternalStorageResult(resultCode)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        onActivityResult(requestCode, RESULT_OK, null)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        onActivityResult(requestCode, RESULT_CANCELED, null)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    protected fun hasWriteStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    protected fun requestWriteStoragePermission() {
        EasyPermissions.requestPermissions(this, getString(R.string.request_write_storage),
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    open fun onWriteExternalStorageResult(resultCode: Int) { }
}