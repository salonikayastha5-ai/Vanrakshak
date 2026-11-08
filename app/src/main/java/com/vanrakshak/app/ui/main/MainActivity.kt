package com.vanrakshak.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vanrakshak.app.R
import com.vanrakshak.app.data.local.UserSessionManager
import com.vanrakshak.app.databinding.ActivityMainBinding
import com.vanrakshak.app.ui.alerts.AlertsFragment
import com.vanrakshak.app.ui.auth.AuthActivity
import com.vanrakshak.app.ui.dashboard.DashboardFragment
import com.vanrakshak.app.ui.nodes.NodesFragment
import com.vanrakshak.app.ui.profile.ProfileFragment
import com.vanrakshak.app.ui.zones.ZonesFragment

/**
 * Main Activity hosting Bottom Navigation and 5 core feature fragments:
 * 1. Dashboard
 * 2. Nodes
 * 3. Alerts
 * 4. Zones
 * 5. Profile
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            navigateToAuth()
            return
        }

        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment(), "dashboard")
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    loadFragment(DashboardFragment(), "dashboard")
                    true
                }
                R.id.nav_nodes -> {
                    loadFragment(NodesFragment(), "nodes")
                    true
                }
                R.id.nav_alerts -> {
                    loadFragment(AlertsFragment(), "alerts")
                    true
                }
                R.id.nav_zones -> {
                    loadFragment(ZonesFragment(), "zones")
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment(), "profile")
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    fun selectNavigationTab(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }

    fun logoutUser() {
        sessionManager.logout()
        navigateToAuth()
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
