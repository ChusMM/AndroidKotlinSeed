package com.example.androidkotlinseed.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.example.androidkotlinseed.R
import com.squareup.picasso.*
import com.squareup.picasso.Target
import java.lang.Exception

class ImageLoader {

    companion object {
        private val TAG = ImageLoader::class.java.simpleName
    }

    interface LoadFinishListener {
        fun onImageLoaded()
    }

    private fun getStandardInstance(uri: String): RequestCreator {
        return this.getStandardInstance(uri, R.drawable.placeholder)
    }

    private fun getStandardInstance(uri: String, @DrawableRes placeHolder: Int): RequestCreator {
        return Picasso.get()
            .load(uri)
            .placeholder(placeHolder)
            .error(placeHolder)
    }

    private fun getOfflineInstance(uri: String): RequestCreator {
        return this.getStandardInstance(uri).networkPolicy(NetworkPolicy.OFFLINE)
    }

    private fun getOfflineInstance(uri: String, @DrawableRes placeHolder: Int): RequestCreator {
        return this.getStandardInstance(uri, placeHolder).networkPolicy(NetworkPolicy.OFFLINE)
    }

    fun loadFromUrl(url: String, target: ImageView) {
        loadFromUrl(url, target, null)
    }

    fun loadFromUrl(url: String, target: ImageView, loadFinishListener: LoadFinishListener?) {
        getOfflineInstance(url).into(object: Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                target.setImageDrawable(placeHolderDrawable)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable) {
                target.setImageDrawable(errorDrawable)
            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                target.setImageBitmap(bitmap)
                loadFinishListener?.onImageLoaded()
            }
        })
    }

    fun loadFromUrlFor43AspectRatio(url: String, target: ImageView,
                                    @DrawableRes placeHolder: Int) {
        val layoutParams = target.layoutParams

        val widthPx = if (target.measuredWidth != 0) target.measuredWidth else 500
        val resizedHeight = (3 * widthPx) / 4

        layoutParams.height = resizedHeight
        target.layoutParams = layoutParams

        target.visibility = View.VISIBLE

        getOfflineInstance(url, placeHolder)
            .resize(widthPx, resizedHeight)
            .into(target, object: Callback {
                override fun onSuccess() { }

                override fun onError(e: Exception) {
                    getStandardInstance(url, placeHolder)
                        .resize(widthPx, resizedHeight)
                        .into(target)
                }
            })
    }

    fun setImageCircularBitmap(imageView: ImageView, bitmap: Bitmap) {
        try {
            val circularBitMap = getCroppedBitmap(bitmap)
            imageView.setImageBitmap(circularBitMap)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color

        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2,
            bitmap.width.toFloat() / 2, paint
        )

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }
}