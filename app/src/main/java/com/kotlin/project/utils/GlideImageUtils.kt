package com.kotlin.project.utils

import android.content.Context
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.kotlin.project.MApplication
import com.kotlin.project.R
import com.bumptech.glide.request.target.Target


fun ImageView.loadRound(url: String?, showLoader: Boolean) {
    if (!url.isNullOrEmpty() && context != null) {
        val requestOptions =
            RequestOptions.circleCropTransform()
        loadImage(context, this, url, requestOptions, showLoader)
    }
}

fun ImageView.loadRect(url: String?, showLoader: Boolean) {
    if (!url.isNullOrEmpty() && context != null) {
        val requestOptions =
            RequestOptions.centerCropTransform()
        loadImage(context, this, url, requestOptions, showLoader)
    }
}

fun ImageView.loadRectNoCut(url: String?, showLoader: Boolean) {
    if (!url.isNullOrEmpty() && context != null) {
        val requestOptions =
            RequestOptions.fitCenterTransform()
        loadImage(context, this, url, requestOptions, showLoader)
    }
}

fun ImageView.loadRoundCorner(url: String?, roundedCorners: Int, showLoader: Boolean) {
    if (!url.isNullOrEmpty() && context != null) {
        val requestOptions =
            RequestOptions().transform(CenterCrop(), RoundedCorners(roundedCorners))
        loadImage(context, this, url, requestOptions, showLoader)
    }
}

private fun loadImage(
    context: Context,
    view: ImageView,
    url: String?,
    requestOptions: RequestOptions,
    showLoader: Boolean
) {
    if (!MApplication.app.isContextExisted(context)) {
        return
    }

    try {
        if (showLoader) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 6F
            circularProgressDrawable.setColorFilter(
                MApplication.applicationContext.resources.getColor(
                    R.color.colorAccent
                ), PorterDuff.Mode.SRC
            )
            circularProgressDrawable.centerRadius = 40F
            circularProgressDrawable.start()

            Glide.with(context)
                .load(url)
                .thumbnail(0.2f)
                .apply(requestOptions)
                .placeholder(circularProgressDrawable)
                .into(view)
        } else
            Glide.with(context)
                .load(url)
                .thumbnail(0.2f)
                .apply(requestOptions)
                .into(view)
    } catch (e: Exception) {
        LogUtils.e(e)
    }
}

fun loadGif(context: Context, gifView: ImageView, gifDrawable: Int, loopCount: Int) {
    Glide.with(context).asGif().load(gifDrawable)
        .listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<GifDrawable>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable,
                model: Any,
                target: Target<GifDrawable>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                resource.setLoopCount(loopCount)
                return false
            }
        }).into(gifView).clearOnDetach()
}

fun clearMemory(context: Context) {
    Glide.get(context).clearMemory()
}
