package com.example.androidkotlinseed.injection

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinseed.R
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseActivity : AppCompatActivity(),
        EasyPermissions.PermissionCallbacks {

    companion object {
        const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1001
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

    open fun onWriteExternalStorageResult(resultCode: Int) {}
}