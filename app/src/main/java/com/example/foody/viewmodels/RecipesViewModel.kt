package com.example.foody.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.foody.util.Constants
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constants.Companion.QUERY_API_KEY
import com.example.foody.util.Constants.Companion.QUERY_DIET
import com.example.foody.util.Constants.Companion.QUERY_FILL_INGREDIENT
import com.example.foody.util.Constants.Companion.QUERY_NUMBER
import com.example.foody.util.Constants.Companion.QUERY_RECIPE_INFORMATION
import com.example.foody.util.Constants.Companion.QUERY_TYPE

class RecipesViewModel(application : Application) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        var query : HashMap<String, String> = HashMap()

        query[QUERY_NUMBER] = "50"
        query[QUERY_API_KEY] = Constants.API_KEY
        query[QUERY_TYPE] = DEFAULT_MEAL_TYPE
        query[QUERY_DIET] = DEFAULT_DIET_TYPE
        query[QUERY_RECIPE_INFORMATION] = "true"
        query[QUERY_FILL_INGREDIENT] = "true"

        return query
    }

}