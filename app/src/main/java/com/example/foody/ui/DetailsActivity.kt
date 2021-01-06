package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.ui.fragments.ingredients.IngredientsFragment
import com.example.foody.ui.fragments.instructions.InstructionsFragment
import com.example.foody.ui.fragments.overview.OverviewFragment
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //region setup of the action bar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //endregion

        //region fragment Pager Adapter setup
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(OverviewFragment())
        fragmentList.add(IngredientsFragment())
        fragmentList.add(InstructionsFragment())

        val fragmentTitleList = ArrayList<String>()
        fragmentTitleList.add("Overview")
        fragmentTitleList.add("Ingredients")
        fragmentTitleList.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle, fragmentList, fragmentTitleList, supportFragmentManager
        )
        viewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewpager)
        //endregion
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // finish the activity when pressed the action bar back button
        if (item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }
}