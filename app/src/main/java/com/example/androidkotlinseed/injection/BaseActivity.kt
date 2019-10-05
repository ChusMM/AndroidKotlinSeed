package com.example.androidkotlinseed.injection

import android.content.Intent
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinseed.App
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationModule
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
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
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE -> onRequestWriteExternalStorage(resultCode)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        onActivityResult(requestCode, RESULT_OK, null)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        onActivityResult(requestCode, RESULT_CANCELED, null)
    }

    open fun onRequestWriteExternalStorage(resultCode: Int) { }
}