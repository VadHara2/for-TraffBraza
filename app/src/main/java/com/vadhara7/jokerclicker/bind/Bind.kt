package com.vadhara7.jokerclicker.bind

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object Bind {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImage(imgView: ImageView, imgId: Int?) {
        imgId?.let {
            Glide.with(imgView.context)
                .load(imgId)
                .into(imgView)
        }
    }
}