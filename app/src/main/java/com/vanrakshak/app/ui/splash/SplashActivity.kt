package com.vanrakshak.app.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vanrakshak.app.data.local.UserSessionManager
import com.vanrakshak.app.databinding.ActivitySplashBinding
import com.vanrakshak.app.ui.auth.AuthActivity
import com.vanrakshak.app.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Premium Nature-inspired Splash Screen for VanRakshak AI Forest Protection System.
 * Displays emblem, tagline, and loading state for 2 seconds before navigating to Auth or Main.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: UserSessionManager

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)
        scheduleNextScreenNavigation()
    }

    private fun scheduleNextScreenNavigation() {
        lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            if (!isFinishing && !isDestroyed) {
                navigateToNextScreen()
            }
        }
    }

    private fun navigateToNextScreen() {
        val targetClass = if (sessionManager.isLoggedIn()) {
            MainActivity::class.java
        } else {
            AuthActivity::class.java
        }
        val intent = Intent(this@SplashActivity, targetClass)
        startActivity(intent)
        finish()
    }
}
