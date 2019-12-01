package com.example.androidkotlinseed.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.androidkotlinseed.BuildConfig
import com.example.androidkotlinseed.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.io.*
import java.lang.Exception

class ImageUtils(private val activity: Activity,
                 private val appRxSchedulers: AppRxSchedulers) {

    companion object {
        private val TAG = ImageUtils::class.java.simpleName
    }

    fun shareImageVia(bitmap: Bitmap, imageName: String): Disposable {
        return writeImageInCacheStorage(bitmap, imageName) { imageFile, error ->
            if (error == null && imageFile != null) {
                actionShare(imageFile)
            } else {
                Log.e(TAG, "Could not share image, reason: " + error?.stackTrace)
            }
        }
    }

    private fun writeImageInCacheStorage(bitmap: Bitmap, imageName: String, callback: (File?, Throwable?) -> Unit): Disposable {
        val fileObservable = writeInto(createCacheImagesFolder(), imageName, bitmap, appRxSchedulers.disk)

        return fileObservable.subscribeOn(appRxSchedulers.disk)
            .observeOn(appRxSchedulers.main)
            .subscribe({ path -> callback(path, null) }, { t -> callback(null, t) } )
    }

    private fun actionShare(imageFile: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"

        val uri = FileProvider.getUriForFile(activity,
            BuildConfig.APPLICATION_ID + ".provider", File(imageFile.absolutePath))

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_image_via)))
    }

    private fun createCacheImagesFolder(): File {
        activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let { picturesDir ->
            File("${picturesDir.absolutePath}/AndroidSeedImages")
        }?.let {
            if (!it.exists() && !it.mkdir()) {
                Log.e(TAG, "Unable to create pictures folder")
                throw IOException("Cannot create image Directory")
            }
            return it
        } ?: run {
            Log.e(TAG, "Pictures device folder not found")
            throw IOException("Cannot create image Directory")
        }
    }

    private fun writeInto(directory: File, fileName: String, bitmap: Bitmap, scheduler: Scheduler): Observable<File> {
        val handler = ObservableOnSubscribe<File> { emitter ->
            val disposable = scheduler.createWorker().schedule {

                val byteArrayOutputStream = ByteArrayOutputStream()
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                } catch (e: Exception) {
                    if (!emitter.isDisposed) emitter.onError(e)
                    return@schedule
                }

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
                    if (!emitter.isDisposed) emitter.onNext(file)
                } catch (e: IOException) {
                    if (!emitter.isDisposed) emitter.onError(e)
                } finally {
                    fileOutputStream?.close()
                }
            }
            emitter.setDisposable(disposable)
        }
        return Observable.create(handler)
    }
}