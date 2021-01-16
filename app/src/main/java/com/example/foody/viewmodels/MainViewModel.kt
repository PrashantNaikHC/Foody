package com.example.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foody.data.Repository
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.models.FoodJoke
import com.example.foody.models.FoodRecipe
import com.example.foody.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    //region Room database
    val readRecipes : LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavouriteRecipes : LiveData<List<FavouritesEntity>> = repository.local.readFavouriteRecipe().asLiveData()

    private fun insertRecipesToDB(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipesEntity)
    }

    fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity)= viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFavouriteRecipes(favouritesEntity)
    }

    fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)= viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteFavouriteRecipe(favouritesEntity)
    }

    fun deleteAllFavouriteRecipe()= viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavouriteRecipe()
    }
    //endregion

    //region Retrofit
    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(query: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(query)
    }

    fun getSearchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        getSearchRecipesSafeCall(searchQuery)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }
    //endregion

    /**
     * Safe call methods:
     * add the caching mechanism if there is any here
     */
    //region safe network calls
    private suspend fun getRecipesSafeCall(query: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(query)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private suspend fun getSearchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getSearchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Food joke not found")
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error("No internet connection")
        }
    }
    //endregion

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipesToDB(recipesEntity)
    }
    /**
     * handle network call methods:
     * Returns the NetworkResult from the response
     */
    //region handle network responses from safe call
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API key is limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodResponse = response.body()
                return NetworkResult.Success(foodResponse!!)
            }
            else -> return NetworkResult.Error(response.message())
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>) : NetworkResult<FoodJoke> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API key is limited")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke!!)
            }
            else -> NetworkResult.Error(response.message())
        }
    }
    //endregion

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}