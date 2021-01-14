package com.example.foody.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.adapters.FavouriteRecipesAdapter
import com.example.foody.databinding.FragmentFavoriteRecipesBinding
import com.example.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    val mAdapter: FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter() }
    val mainViewModel: MainViewModel by viewModels()

    private var _binding : FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // val rootView = inflater.inflate(R.layout.fragment_favorite_recipes, container, false)
        // using the binding instead of the view
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)

        // setting the lifecycle owner explicitely
        binding.lifecycleOwner = this

        // setting up the binding for the layout elements
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        //setupRecyclerView(rootView.favourite_recipes_recyclerview)
        setupRecyclerView(binding.favouriteRecipesRecyclerview)

        // below is not required since it is taken care in the binding adapter
        /*mainViewModel.readFavouriteRecipes.observe(viewLifecycleOwner, { favouritesList ->
            mAdapter.setData(favouritesList)
        })*/

        //return rootView
        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}