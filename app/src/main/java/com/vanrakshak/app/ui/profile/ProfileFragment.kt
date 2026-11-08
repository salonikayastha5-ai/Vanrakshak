package com.vanrakshak.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vanrakshak.app.data.local.UserSessionManager
import com.vanrakshak.app.databinding.FragmentProfileBinding
import com.vanrakshak.app.ui.main.MainActivity

/**
 * Enhanced Profile & Settings Fragment displaying user profile,
 * notification preferences toggle, system info, and logout confirmation dialog.
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = UserSessionManager(requireContext())

        binding.tvProfileName.text = sessionManager.getUserName()
        binding.tvProfileRole.text = sessionManager.getUserRole()
        binding.tvProfileEmail.text = sessionManager.getUserEmail()

        // Notification Switch Listeners
        binding.switchCriticalAlerts.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Snackbar.make(binding.root, "Critical Chainsaw push notifications $status", Snackbar.LENGTH_SHORT).show()
        }

        binding.switchLowBattery.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Snackbar.make(binding.root, "Hardware battery & connectivity alerts $status", Snackbar.LENGTH_SHORT).show()
        }

        // Logout Action with Confirmation Dialog
        binding.btnProfileLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to log out from the VanRakshak Forest Protection Portal?")
            .setPositiveButton("Logout") { _, _ ->
                (activity as? MainActivity)?.logoutUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
