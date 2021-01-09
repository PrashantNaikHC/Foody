package com.example.foody.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.IngredientAdapter
import com.example.foody.models.Result
import com.example.foody.util.Constants.Companion.RECIPE_BUNDLE_RESULT
import kotlinx.android.synthetic.main.fragment_ingredients.view.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*

class IngredientsFragment : Fragment() {

    val mAdapter : IngredientAdapter by lazy { IngredientAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val result : Result? = arguments?.getParcelable(RECIPE_BUNDLE_RESULT)

        setupAdapter(rootView)
        result?.extendedIngredients?.let { mAdapter.setData(it) }
        return rootView
    }

    private fun setupAdapter(rootView: View?) {
        rootView?.ingredients_recyclerview?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}