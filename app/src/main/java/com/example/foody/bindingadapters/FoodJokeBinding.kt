package com.example.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.example.foody.data.database.entities.FoodJokeEntity
import com.example.foody.models.FoodJoke
import com.example.foody.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic
        fun setCardAndProgressvisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    Log.d("FoodJokeBinding", "NetworkResult.Loading ")
                    when (view) {
                        is ProgressBar -> view.visibility = View.VISIBLE
                        is MaterialCardView -> view.visibility = View.INVISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    Log.d("FoodJokeBinding", "NetworkResult.Error ")
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    Log.d("FoodJokeBinding", "NetworkResult.Success ")
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> view.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}