package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.databinding.FavouriteRecipesRowLayoutBinding
import com.example.foody.ui.fragments.FavoriteRecipesFragmentDirections
import com.example.foody.util.RecipeDiffUtil
import kotlinx.android.synthetic.main.favourite_recipes_row_layout.view.*

class FavouriteRecipesAdapter : RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>() {

    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(
        private val binding: FavouriteRecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favouritesEntity: FavouritesEntity) {
            binding.favouritesEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteRecipesRowLayoutBinding.inflate(
                    layoutInflater, parent, false
                )
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedRecipe = favouriteRecipes[position]
        holder.bind(selectedRecipe)
        holder.itemView.favouriteRecipesRowLayout.setOnClickListener {
            val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(selectedRecipe.result)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }

    fun setData(newList : List<FavouritesEntity>){
        val recipesDiffUtil = RecipeDiffUtil(favouriteRecipes, newList)
        favouriteRecipes = newList
        DiffUtil.calculateDiff(recipesDiffUtil).dispatchUpdatesTo(this)
    }
}