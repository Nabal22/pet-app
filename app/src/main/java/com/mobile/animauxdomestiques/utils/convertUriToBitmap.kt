package com.mobile.animauxdomestiques.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri

fun convertUriToBitmap(context: Context, uri: Uri?): Bitmap? {
    return try {
        uri?.let {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
