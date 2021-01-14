package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.ui.fragments.ingredients.IngredientsFragment
import com.example.foody.ui.fragments.instructions.InstructionsFragment
import com.example.foody.ui.fragments.overview.OverviewFragment
import com.example.foody.util.Constants.Companion.RECIPE_BUNDLE_RESULT
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel : MainViewModel by viewModels()

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
        resultBundle.putParcelable(RECIPE_BUNDLE_RESULT, args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle, fragmentList, fragmentTitleList, supportFragmentManager
        )
        viewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewpager)
        //endregion
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // finish the activity when pressed the action bar back button
            android.R.id.home -> finish()
            R.id.save_to_favourites -> saveToFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(0, args.result)
        mainViewModel.insertFavouriteRecipe(favouritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnack("Recipe saved")
    }

    private fun showSnack(text: String) {
        Snackbar.make(details_layout, text, Snackbar.LENGTH_SHORT)
            .setAction("Okay"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }
}