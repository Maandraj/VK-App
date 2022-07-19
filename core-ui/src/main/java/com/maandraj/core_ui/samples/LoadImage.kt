package com.maandraj.core_ui.samples

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


@Composable
fun loadImage(url: String, placeholder: Painter, errorHolder: Painter): Painter {
    var state by remember {
        mutableStateOf(placeholder)
    }
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .autoClone()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
    val context = LocalContext.current
    val result = object : CustomTarget<Bitmap>() {
        override fun onLoadCleared(p: Drawable?) {
            state = placeholder
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            state = errorHolder
        }

        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap>?,
        ) {
            state = BitmapPainter(resource.asImageBitmap())
        }
    }
    try {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .apply(options)
            .into(result)
    } catch (e: Exception) {
        Log.i("LoadImage", e.message.toString())
    }
    return state
}