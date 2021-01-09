package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.R
import com.example.foody.models.ExtendedIngredient
import com.example.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foody.util.RecipeDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {

    var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.itemView.apply {
            ingredient_imageview.load(BASE_IMAGE_URL + ingredient.image) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            ingredient_name.text = ingredient.name
            ingredient_amount.text = ingredient.amount.toString()
            ingredient_unit.text = ingredient.unit
            ingredient_consistency.text = ingredient.consistency
            ingredient_original.text = ingredient.original
        }
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients : List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipeDiffUtil<ExtendedIngredient>(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}