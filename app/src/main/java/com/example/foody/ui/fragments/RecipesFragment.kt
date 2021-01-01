package com.example.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.RecipeAdapter
import com.example.foody.models.FoodRecipe
import com.example.foody.util.Constants.Companion.API_KEY
import com.example.foody.util.NetworkResult
import com.example.foody.util.observeOnce
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private val TAG: String? = "RecipesFragment"
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mRecipesViewModel: RecipesViewModel
    private lateinit var mView: View
    private val mAdapter by lazy { RecipeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mRecipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        setupAdapter()
        readFromDB()
        return mView
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mMainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun readFromDB() {
        lifecycleScope.launch {
            mMainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmer()
                    Log.d(TAG, "Reading from database")
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        mMainViewModel.getRecipes(mRecipesViewModel.applyQueries())
        mMainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            Log.d(TAG, "Reading from Network")
            when (response) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Reading from Network | Success")
                    hideShimmer()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    showMessageToast(response)
                    loadDataFromCache()
                }
                is NetworkResult.Loading -> showShimmer()
            }
        }
    }

    private fun showMessageToast(response: NetworkResult<FoodRecipe>) {
        Toast.makeText(
            requireContext(),
            response.message.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupAdapter() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    fun showShimmer() {
        mView.recyclerview.showShimmer()
    }

    fun hideShimmer() {
        mView.recyclerview.hideShimmer()
    }
}