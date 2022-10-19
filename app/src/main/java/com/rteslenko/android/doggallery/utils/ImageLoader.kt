package com.rteslenko.android.doggallery.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rteslenko.android.doggallery.R

class ImageLoader(private val context: Context) {

    fun load(imageUrl: String, target: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(target)
    }
}