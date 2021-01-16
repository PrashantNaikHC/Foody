package com.example.foody.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.adapters.FavouriteRecipesAdapter
import com.example.foody.databinding.FragmentFavoriteRecipesBinding
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    val mainViewModel: MainViewModel by viewModels()
    val mAdapter: FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity(), mainViewModel) }

    private var _binding : FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // val rootView = inflater.inflate(R.layout.fragment_favorite_recipes, container, false)
        // using the binding instead of the view
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)

        // setting the lifecycle owner explicitely
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_all_favourite_recipes_menu){
            mainViewModel.deleteAllFavouriteRecipe()
            showSnack("All favourite recipes removed.")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnack(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}
            .show()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}