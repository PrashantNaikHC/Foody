package com.example.foody.bindingadapters

import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.foody.data.database.RecipesEntity
import com.example.foody.models.FoodRecipe
import com.example.foody.models.Result
import com.example.foody.ui.fragments.RecipesFragmentDirections
import com.example.foody.util.NetworkResult
import org.jsoup.Jsoup
import java.lang.Exception

class RecipesBinding {
    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: Result) {
            recipeRowLayout.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                } catch (e: Exception){
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = INVISIBLE
            }
        }

        @BindingAdapter("parseHtmlText")
        @JvmStatic
        fun parseHtml(textView: TextView, string: String?){
            string.let {
                val parsedText = Jsoup.parse(it).text()
                textView.text = parsedText
            }
        }
    }
}