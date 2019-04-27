package com.example.androidkotlinseed.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.example.androidkotlinseed.BuildConfig
import com.example.androidkotlinseed.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

class ImageUtils(private val context: Context) {
    private val TAG = ImageUtils::class.java.simpleName

    private val executorService = Executors.newSingleThreadExecutor()

    @UiThread
    fun writeImageInLocalStorage(bitmap: Bitmap, imageName: String,
        writeListener: FileHandler.WriteListener) {

        val handler = FileHandler(writeListener)

        val directory = File(context.cacheDir.toString() + "/SeedImages")
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                Log.e(TAG, "Unable to create Image")
                val throwable = IOException("Cannot create image Directory")
                handler.sendMessage(buildFailedMsg(throwable))
            }
        }

        this.executorService.submit {
            try {
                val path = writeInto(directory, imageName, bitmap)
                bitmap.recycle()
                handler.sendMessage(buildSuccessMsg(path))
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
                handler.sendMessage(buildFailedMsg(e))
            } catch (e: NullPointerException) {
                Log.e(TAG, e.toString())
                handler.sendMessage(buildFailedMsg(e))
            }
        }
    }

    private fun buildFailedMsg(throwable: Throwable): Message {
        val msg = Message()
        msg.what = FileHandler.WRITE_FAILED
        val bundle = Bundle()
        bundle.putSerializable(FileHandler.THROWABLE_KEY, throwable)
        msg.data = bundle

        return msg
    }

    private fun buildSuccessMsg(imagePath: String): Message {
        val msg = Message()
        msg.what = FileHandler.WRITE_OK
        val bundle = Bundle()
        bundle.putString(FileHandler.FILE_PATH_KEY, imagePath)
        msg.data = bundle

        return msg
    }

    @UiThread
    fun actionShare(imageFile: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"

        val uri = FileProvider.getUriForFile(context,
            BuildConfig.APPLICATION_ID + ".provider", File(imageFile.absolutePath)
        )

        intent.putExtra(Intent.EXTRA_STREAM, uri)

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_image_via)))
    }

    @WorkerThread
    @Throws(IOException::class)
    private fun writeInto(directory: File, fileName: String, bitmap: Bitmap): String {

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
            return file.absolutePath
        } catch (e: IOException) {
            throw e
        } finally {
            fileOutputStream?.close()
        }
    }
}