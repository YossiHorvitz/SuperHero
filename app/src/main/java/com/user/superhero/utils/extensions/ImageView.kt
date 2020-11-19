package com.user.superhero.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

fun ImageView.load(imageUrl: String, onClick: (() -> Unit)? = null) {

    Glide.with(this)
        .load(imageUrl)
        .transition(withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this.apply {
            setOnClickListener { onClick?.invoke() }
        })
}