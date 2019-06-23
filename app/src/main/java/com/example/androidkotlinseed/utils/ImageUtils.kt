package com.example.androidkotlinseed.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import androidx.core.content.FileProvider
import com.example.androidkotlinseed.BuildConfig
import com.example.androidkotlinseed.R
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageUtils(private val activity: Activity,
                 private val appRxSchedulers: AppRxSchedulers) {
    private val TAG = ImageUtils::class.java.simpleName

    fun shareImageVia(bitmap: Bitmap, imageName: String) {
        this.writeImageInCacheStorage(bitmap, imageName) { imageFile, error ->
            if (error != null && imageFile != null) {
                actionShare(imageFile)
            } else {
                Log.e(TAG, "Could not share image, reason: " + error?.stackTrace)
            }
        }
    }

    private fun writeImageInCacheStorage(bitmap: Bitmap, imageName: String, callback: (File?, Throwable?) -> Unit): Disposable {
        return Observable.just(writeInto(createCacheImagesFolder(), imageName, bitmap))
            .subscribeOn(appRxSchedulers.disk)
            .observeOn(appRxSchedulers.main)
            .subscribe({ path -> callback(path, null) }, { t -> callback(null, t) } )
    }

    private fun actionShare(imageFile: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"

        val uri = FileProvider.getUriForFile(activity,
            BuildConfig.APPLICATION_ID + ".provider", File(imageFile.absolutePath)
        )

        intent.putExtra(Intent.EXTRA_STREAM, uri)

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_image_via)))
    }

    private fun createCacheImagesFolder(): File {
        val folder = File(activity.cacheDir.toString() + "/AndroidSeedImages")
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e(TAG, "Unable to create Image")
                throw IOException("Cannot create image Directory")
            }
        }
        return folder
    }

    private fun writeInto(directory: File, fileName: String, bitmap: Bitmap): File {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            throw IOException("Write file called from main thread")
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val file = File(directory, fileName)
        val isCreated = file.createNewFile()

        var fileOutputStream: FileOutputStream? = null
        try {
            if (isCreated) {
                fileOutputStream = FileOutputStream(file)
                fileOutputStream.write(byteArrayOutputStream.toByteArray())
            } else {
                throw IOException("Could not create image file")
            }
            return file
        } catch (e: IOException) {
            throw e
        } finally {
            fileOutputStream?.close()
        }
    }
}