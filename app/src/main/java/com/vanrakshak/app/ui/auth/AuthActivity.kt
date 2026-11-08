package com.vanrakshak.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.vanrakshak.app.R
import com.vanrakshak.app.data.local.UserSessionManager
import com.vanrakshak.app.databinding.ActivityAuthBinding
import com.vanrakshak.app.ui.main.MainActivity

/**
 * Enhanced Authentication Activity with distinct Login & Registration UI flows,
 * interactive dialogs, password recovery, and input validation.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var sessionManager: UserSessionManager
    private var isRegisterMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)

        setupRoleDropdown()
        setupListeners()
        updateUiForMode()
    }

    private fun setupRoleDropdown() {
        val roles = arrayOf(
            "Forest Ranger / Authority",
            "Forest Guard Field Officer",
            "System Administrator",
            "Environmental Researcher"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        binding.actvRole.setAdapter(adapter)
    }

    private fun setupListeners() {
        // Toggle between Login and Register modes
        binding.tvToggleMode.setOnClickListener {
            isRegisterMode = !isRegisterMode
            updateUiForMode()
        }

        // Forgot Password dialog
        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Submit Action
        binding.btnAuthSubmit.setOnClickListener {
            handleAuthSubmit()
        }
    }

    private fun updateUiForMode() {
        binding.tvErrorMsg.visibility = View.GONE

        if (isRegisterMode) {
            // REGISTRATION MODE UI
            binding.tvHeaderSubtitle.text = getString(R.string.auth_register_subtitle)
            binding.tvFormTitle.text = getString(R.string.auth_create_account)
            binding.tvFormSubtitle.text = getString(R.string.auth_register_subtitle)
            binding.tilName.visibility = View.VISIBLE
            binding.tilRole.visibility = View.VISIBLE
            binding.tilConfirmPassword.visibility = View.VISIBLE
            binding.layoutRegisterTerms.visibility = View.VISIBLE
            binding.layoutLoginOptions.visibility = View.GONE
            binding.btnAuthSubmit.text = getString(R.string.auth_btn_register)
            binding.tvToggleMode.text = getString(R.string.auth_link_to_login)
        } else {
            // LOGIN MODE UI
            binding.tvHeaderSubtitle.text = getString(R.string.auth_login_subtitle)
            binding.tvFormTitle.text = getString(R.string.auth_welcome_back)
            binding.tvFormSubtitle.text = getString(R.string.auth_login_subtitle)
            binding.tilName.visibility = View.GONE
            binding.tilRole.visibility = View.GONE
            binding.tilConfirmPassword.visibility = View.GONE
            binding.layoutRegisterTerms.visibility = View.GONE
            binding.layoutLoginOptions.visibility = View.VISIBLE
            binding.btnAuthSubmit.text = getString(R.string.auth_btn_login)
            binding.tvToggleMode.text = getString(R.string.auth_link_to_register)
        }
    }

    private fun handleAuthSubmit() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()
        val confirmPassword = binding.etConfirmPassword.text?.toString()?.trim().orEmpty()
        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val role = binding.actvRole.text.toString().trim()

        binding.tvErrorMsg.visibility = View.GONE

        if (isRegisterMode) {
            if (name.isBlank()) {
                showError(getString(R.string.auth_err_name))
                return
            }
        }

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(getString(R.string.auth_err_email))
            return
        }

        if (password.length < 6) {
            showError(getString(R.string.auth_err_password))
            return
        }

        if (isRegisterMode) {
            if (password != confirmPassword) {
                showError(getString(R.string.auth_err_password_match))
                return
            }

            if (!binding.cbAcceptTerms.isChecked) {
                showError(getString(R.string.auth_err_terms))
                return
            }
        }

        val displayName = if (isRegisterMode && name.isNotBlank()) {
            name
        } else {
            email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
        val displayRole = if (role.isNotBlank()) role else "Forest Authority"

        sessionManager.createSession(displayName, email, displayRole)

        navigateToMain()
    }

    private fun showForgotPasswordDialog() {
        val emailInput = binding.etEmail.text?.toString()?.trim().orEmpty()

        AlertDialog.Builder(this)
            .setTitle("Password Recovery")
            .setMessage("Demo Mode: To reset password for '$emailInput', please contact your Division System Administrator or use demo credentials.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                Snackbar.make(binding.root, "Password reset instructions sent to your email", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showError(message: String) {
        binding.tvErrorMsg.text = message
        binding.tvErrorMsg.visibility = View.VISIBLE
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
