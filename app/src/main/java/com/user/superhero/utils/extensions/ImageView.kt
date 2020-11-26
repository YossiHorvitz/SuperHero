package com.user.superhero.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.user.superhero.utils.AppUtils.applyPalette
import com.user.superhero.utils.AppUtils.saveImage

fun ImageView.load(imageUrl: String, onClick: (() -> Unit)? = null) {

    Glide.with(this)
        .load(imageUrl)
        .transition(withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this.apply {
            setOnClickListener { onClick?.invoke() }
        })
}

fun ImageView.loadImage(activity: AppCompatActivity, imageUrl: String) {

    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .transition(withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                resource?.toBitmap().let {
                    if (it != null) saveImage(context, it)
                    if (it != null) applyPalette(activity, it)
                }
                return false
            }
        })
        .into(this)
}