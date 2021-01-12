package com.example.foody.data

import com.example.foody.data.database.RecipesDao
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){
    //region Recipes database
    suspend fun insertRecipes(recipesEntity : RecipesEntity){
        recipesDao.insertRecipe(recipesEntity)
    }

    fun readRecipes() : Flow<List<RecipesEntity>> {
        return recipesDao.readRecipe()
    }
    //endregion

    //region Favourite recipes database
    fun readFavouriteRecipe(): Flow<List<FavouritesEntity>> {
        return recipesDao.readFavouriteRecipe()
    }

    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity){
        recipesDao.insertFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity){
        recipesDao.deleteFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteAllFavouriteRecipe(){
        recipesDao.deleteAllFavouriteRecipes()
    }
    //endregion
}