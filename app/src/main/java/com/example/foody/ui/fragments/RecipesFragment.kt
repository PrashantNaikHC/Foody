package com.example.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.RecipeAdapter
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.models.FoodRecipe
import com.example.foody.util.NetworkListener
import com.example.foody.util.NetworkResult
import com.example.foody.util.observeOnce
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val TAG: String? = "RecipesFragment"

    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mRecipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipeAdapter() }
    lateinit var networkListener: NetworkListener

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

        setHasOptionsMenu(true)
        setupAdapter()

        mRecipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            mRecipesViewModel.backOnline = it
        })

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    mRecipesViewModel.networkStatus = status
                    mRecipesViewModel.showNetworkStatus()
                    readFromDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (mRecipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                mRecipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        //setup for the search view implementation
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
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

    private fun readFromDatabase() {
        lifecycleScope.launch {
            mMainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                // load from the network only if Apply button is clicked
                // else if it is dismissed, load from the database
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
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

    private fun searchApiData(query: String) {
        showShimmer()
        mMainViewModel.getSearchRecipes(mRecipesViewModel.applySearchQueries(query))
        mMainViewModel.searchRecipesResponse.observe(viewLifecycleOwner , { response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    loadDataFromCache()
                    showMessageToast(response)
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }
        })
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