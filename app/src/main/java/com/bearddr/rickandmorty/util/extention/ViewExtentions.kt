package com.bearddr.rickandmorty.util.extention

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadFromUrl(url: String) {
    Glide
        .with(this)
        .load(url)
        .circleCrop()
        .into(this)
}