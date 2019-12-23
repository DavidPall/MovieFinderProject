package com.example.moviefinderproject.View

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviefinderproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PARAM_NAVIGATION_ID = "navigation_id"

        fun newInstance(context: Context, navigationId: Int) =  Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(PARAM_NAVIGATION_ID, navigationId)
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        loadFragment(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        loadFragment(navigation.selectedItemId)
        loadFragment(R.id.navigation_dashboard)
        val navigationId = intent.getIntExtra(
            PARAM_NAVIGATION_ID,
            R.id.navigation_dashboard
        )
        navigation.selectedItemId = navigationId
    }



    private fun loadFragment(itemId: Int) {
        val tag = itemId.toString()
        var fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (itemId) {
            R.id.navigation_trending -> {
                TrendingFragment.newInstance()
            }
            R.id.navigation_profile -> {
                ProfileFragment.newInstance()
            }
            R.id.navigation_favourites -> {
                FavouriteFragment.newInstance()
            }
            else -> {
                null
            }
        }

        // replace fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit()
        }
    }
}
