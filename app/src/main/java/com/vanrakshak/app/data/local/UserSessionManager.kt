package com.vanrakshak.app.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages local user session state for VanRakshak demonstration authentication flow.
 */
class UserSessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "vanrakshak_user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROLE = "user_role"
    }

    fun createSession(name: String, email: String, role: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_NAME, name.ifBlank { "Forest Officer" })
            .putString(KEY_USER_EMAIL, email.ifBlank { "officer@vanrakshak.gov.in" })
            .putString(KEY_USER_ROLE, role.ifBlank { "Forest Authority" })
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "Forest Officer") ?: "Forest Officer"

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "officer@vanrakshak.gov.in") ?: "officer@vanrakshak.gov.in"

    fun getUserRole(): String = prefs.getString(KEY_USER_ROLE, "Forest Authority") ?: "Forest Authority"

    fun logout() {
        prefs.edit().clear().apply()
    }
}
