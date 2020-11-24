package com.example.githubviewer.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Custom binding adapter for async image loading.
 */

object ImageLoader {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {

        val progressDrawable = CircularProgressDrawable(view.context)
        progressDrawable.strokeWidth = 10f
        progressDrawable.centerRadius = 50f
        progressDrawable.start()

        val requestOptions: RequestOptions = RequestOptions()
            .placeholder(progressDrawable)

        Glide.with(view.context)
            .setDefaultRequestOptions(requestOptions)
            .load(url)
            .into(view)
    }
}