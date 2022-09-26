package com.anless.rentmotors.ui

import android.os.Bundle
import android.view.View
import com.anless.rentmotors.R
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.anless.rentmotors.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Rentmotors)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.startBookingFragment,
                R.id.stationsFragment,
                R.id.chatFragment,
                R.id.profileFragment
            )
        )

        binding.bottomNavigation.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvTitle.text = destination.label

            if (destination.id in appBarConfiguration.topLevelDestinations) {
                with(binding) {
                    imgLogo.visibility = View.VISIBLE
                    tvTitle.visibility = View.GONE
                }
            } else {
                with(binding) {
                    imgLogo.visibility = View.GONE
                    tvTitle.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setSubTitle(subTitle: String?) {
        if (subTitle.isNullOrBlank()) {
            binding.tvSubTitle.visibility = View.GONE
        } else {
            binding.tvSubTitle.text = subTitle
            binding.tvSubTitle.visibility = View.VISIBLE
        }
    }

    fun setToolbarVisible(visible: Boolean) {
        binding.toolbar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}