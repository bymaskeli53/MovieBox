package com.example.moviebox.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.moviebox.R
import com.example.moviebox.databinding.ActivityMainBinding
import com.example.moviebox.util.extension.gone
import com.example.moviebox.util.extension.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)

        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        setBottomNavVisibilityForEachFragment(navController)
    }

    override fun onSupportNavigateUp(): Boolean = super.onSupportNavigateUp() || navController.navigateUp()

    private fun setBottomNavVisibilityForEachFragment(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> {
                    binding.bottomNavView.gone()
                }

                else -> {
                    binding.bottomNavView.show()
                }
            }
        }
    }
}
