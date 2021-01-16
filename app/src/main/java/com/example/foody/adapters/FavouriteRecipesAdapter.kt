package com.example.foody.adapters

import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.databinding.FavouriteRecipesRowLayoutBinding
import com.example.foody.ui.fragments.FavoriteRecipesFragmentDirections
import com.example.foody.util.RecipeDiffUtil
import kotlinx.android.synthetic.main.favourite_recipes_row_layout.view.*
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

// add the fragmentActivity as the param since contextual action requires it
class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(),
    ActionMode.Callback {

    private val TAG: String = "FavouriteRecipesAdapter"

    private var favouriteRecipes = emptyList<FavouritesEntity>()
    private var selectedRecipes = arrayListOf<FavouritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    private var multiSelection = false

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
        myViewHolders.add(holder)
        val currentRecipe = favouriteRecipes[position]
        holder.bind(currentRecipe)
        holder.itemView.favouriteRecipesRowLayout.apply {
            //region click listeners
            setOnClickListener {
                if (multiSelection) {
                    applySelection(holder, currentRecipe)
                } else {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                            currentRecipe.result
                        )
                    holder.itemView.findNavController().navigate(action)
                }
            }

            setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this@FavouriteRecipesAdapter)
                    applySelection(holder, currentRecipe)
                    true
                } else {
                    multiSelection = false
                    false
                }
            }
            //endregion
        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        setStatusBarColor(R.color.contextualStatusBarColor)

        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.card_background_color, R.color.stroke_color)
        }
        multiSelection = false
        selectedRecipes.clear()
        setStatusBarColor(R.color.statusBarColor)
    }

    fun setData(newList: List<FavouritesEntity>) {
        val recipesDiffUtil = RecipeDiffUtil(favouriteRecipes, newList)
        favouriteRecipes = newList
        DiffUtil.calculateDiff(recipesDiffUtil).dispatchUpdatesTo(this)
    }

    private fun setStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.card_background_color, R.color.stroke_color)
        } else {
            selectedRecipes.add(currentRecipe)
            Log.d(TAG, "applySelection: ")
            changeRecipeStyle(holder, R.color.card_background_light_color, R.color.colorPrimary)
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favourite_recipe_row_layout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.itemView.favourite_row_cardview.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

}