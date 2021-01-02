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
import com.example.foody.databinding.FragmentRecipesBinding
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
    private var _binding : FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mRecipesViewModel: RecipesViewModel
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
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mMainViewModel

        setupAdapter()
        readFromDB()
        return binding.root
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
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    fun showShimmer() {
        binding.recyclerview.showShimmer()
    }

    fun hideShimmer() {
        binding.recyclerview.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}