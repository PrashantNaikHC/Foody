package com.example.foody.util

class Constants {
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "33558026c964433e9e0d0e32c0ca2a1e"

        const val RECIPE_BUNDLE_RESULT = "recipeBundle"

        // Query
        const val QUERY_SEARCH = "query"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_NUMBER = "number"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENT = "fillIngredients"

        // Room database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FAVOURITE_RECIPES_TABLE = "favourite_recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"

        // Bottomsheet and preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

        const val PREFERENCE_BACK_ONLINE = "backOnline"
    }
}