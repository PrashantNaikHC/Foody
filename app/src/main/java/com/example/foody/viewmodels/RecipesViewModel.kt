package com.example.foody.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.data.DataStoreRepository
import com.example.foody.util.Constants
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foody.util.Constants.Companion.QUERY_API_KEY
import com.example.foody.util.Constants.Companion.QUERY_DIET
import com.example.foody.util.Constants.Companion.QUERY_FILL_INGREDIENT
import com.example.foody.util.Constants.Companion.QUERY_NUMBER
import com.example.foody.util.Constants.Companion.QUERY_RECIPE_INFORMATION
import com.example.foody.util.Constants.Companion.QUERY_SEARCH
import com.example.foody.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    //region saving Meal and Diet type
    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    fun saveMealAndDietType(mT: String, mTid: Int, dT: String, dTid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mT, mTid, dT, dTid)
        }

    }
    //endregion

    //region Apply queries
    fun applyQueries(): HashMap<String, String> {
        val query: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        query[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        query[QUERY_API_KEY] = Constants.API_KEY
        query[QUERY_TYPE] = mealType
        query[QUERY_DIET] = dietType
        query[QUERY_RECIPE_INFORMATION] = "true"
        query[QUERY_FILL_INGREDIENT] = "true"

        return query
    }

    fun applySearchQueries(searchQuery: String): Map<String, String> {
        val query: HashMap<String, String> = HashMap()

        query[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        query[QUERY_SEARCH] = searchQuery
        query[QUERY_API_KEY] = Constants.API_KEY
        query[QUERY_RECIPE_INFORMATION] = "true"
        query[QUERY_FILL_INGREDIENT] = "true"

        return query
    }
    //endregion

    //region Network status listening
    var networkStatus = false
    var backOnline = false
    val readBackOnline = dataStoreRepository.readBackOnline().asLiveData()

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
    //endregion
}