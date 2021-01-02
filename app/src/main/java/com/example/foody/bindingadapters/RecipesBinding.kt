package com.example.foody.bindingadapters

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody.data.database.RecipesEntity
import com.example.foody.models.FoodRecipe
import com.example.foody.util.NetworkResult

class RecipesBinding {
    companion object {

        @BindingAdapter("readApiResponse","readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty() ){
                imageView.visibility = VISIBLE
            } else if(apiResponse is NetworkResult.Loading){
                imageView.visibility = INVISIBLE
            } else if(apiResponse is NetworkResult.Success){
                imageView.visibility = INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2","readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty() ){
                textView.visibility = VISIBLE
                textView.text = apiResponse.message.toString()
            } else if(apiResponse is NetworkResult.Loading){
                textView.visibility = INVISIBLE
            } else if(apiResponse is NetworkResult.Success){
                textView.visibility = INVISIBLE
            }
        }
    }
}