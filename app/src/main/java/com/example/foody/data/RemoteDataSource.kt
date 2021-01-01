package com.example.foody.data

import com.example.foody.data.network.FoodRecipeApi
import com.example.foody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    val foodRecipeApi: FoodRecipeApi
){

    suspend fun getRecipes(queries : Map<String, String>) : Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }
}