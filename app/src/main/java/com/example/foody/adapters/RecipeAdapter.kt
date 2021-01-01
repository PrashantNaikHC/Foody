package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.databinding.RecipesRowLayoutBinding
import com.example.foody.models.FoodRecipe
import com.example.foody.models.Result
import com.example.foody.util.RecipeDiffUtil

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    var recipeList = emptyList<Result>()

    class MyViewHolder(
        private val binding: RecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            // this is for the onCreateViewHolder to return the viewModel
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val bindingView = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(bindingView)
            }
        }

        // take the result from this class and bind it to the layout variable
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipeList[position]
        holder.bind(currentRecipe)
    }

    fun setData(newData: FoodRecipe) {
        val diffUtil = RecipeDiffUtil(recipeList, newData.results)
        DiffUtil.calculateDiff(diffUtil).dispatchUpdatesTo(this)
        recipeList = newData.results

        // below is replaced with diffutil since it is an overkill
        //notifyDataSetChanged()
    }
}