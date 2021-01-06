package com.example.foody.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.models.Result
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val result: Result? = arguments?.getParcelable("recipeBundle")

        view.apply {
            main_imageview.load(result?.image)
            overview_title_textview.text = result?.title
            likes_textview.text = result?.aggregateLikes.toString()
            time_textview.text = result?.readyInMinutes.toString()
            summary_textview.text = result?.summary

            if (result?.vegetarian == true) {
                markInGreen(vegetarian_imageview, vegetarian_textview)
            }
            if (result?.vegan == true) {
                markInGreen(vegan_imageview, vegan_textview)
            }
            if (result?.glutenFree == true) {
                markInGreen(glutenfree_imageview, glutenfree_textview)
            }
            if (result?.dairyFree == true) {
                markInGreen(diaryfree_imageview, diaryfree_textview)
            }
            if (result?.veryHealthy == true) {
                markInGreen(healthy_imageview, healthy_textview)
            }
            if (result?.cheap == true) {
                markInGreen(cheap_imageview, cheap_textview)
            }
        }
        return view
    }

    private fun markInGreen(imageView: ImageView?, textview: TextView?) {
        imageView?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        textview?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }

}