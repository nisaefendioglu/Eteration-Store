package com.nisaefendioglu.eterationstore.presentation

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nisaefendioglu.eterationstore.R
import com.nisaefendioglu.eterationstore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                getColor(R.color.blue),
                getColor(R.color.blue)
            )
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupNavigation()
        observeBadgeCounts()
    }


    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
                    as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }


    private fun observeBadgeCounts() {
        observeBadge(viewModel.badgeCount, R.id.navigation_cart)
        observeBadge(viewModel.favCount, R.id.navigation_favorites)
    }

    private fun observeBadge(flow: kotlinx.coroutines.flow.Flow<Int>, menuItemId: Int) {
        lifecycleScope.launch {
            flow.collectLatest { count ->
                if (count > 0) {
                    binding.navView.getOrCreateBadge(menuItemId).number = count
                } else {
                    binding.navView.removeBadge(menuItemId)
                }
            }
        }
    }
}
