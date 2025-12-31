package com.muhammad.memes.meme_editor.presentation.util

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.muhammad.memes.MemeApplication
import java.io.ByteArrayOutputStream

fun getImageBytesFromResources(
    @DrawableRes resId : Int
) : ByteArray{
    val context = MemeApplication.INSTANCE
    val drawable = ContextCompat.getDrawable(context, resId)!!
    val bitmap= drawable.toBitmap()
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}