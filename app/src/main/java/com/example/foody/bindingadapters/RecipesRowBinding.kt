package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.foody.R

class RecipesRowBinding {
    companion object {
        @BindingAdapter("setLikes")
        @JvmStatic
        fun setLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setDuration")
        @JvmStatic
        fun setDuration(textView: TextView, duration: Int) {
            textView.text = duration.toString()
        }

        @BindingAdapter("setVegan")
        @JvmStatic
        fun setVegan(view: View, vegan: Boolean) {
            val greenColor = ContextCompat.getColor(view.context, R.color.green)
            if (vegan) {
                when (view) {
                    is TextView -> view.setTextColor(greenColor)
                    is ImageView -> view.setColorFilter(greenColor)
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView : ImageView, imageUrl : String){
            imageView.load(imageUrl) {
                crossfade(600)
            }
        }
    }
}