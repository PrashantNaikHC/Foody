package com.example.foody.data

import com.example.foody.data.database.RecipesDao
import com.example.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){
    suspend fun insertRecipes(recipesEntity : RecipesEntity){
        recipesDao.insertRecipe(recipesEntity)
    }

    fun readDatabase() : Flow<List<RecipesEntity>> {
        return recipesDao.readRecipe()
    }
}