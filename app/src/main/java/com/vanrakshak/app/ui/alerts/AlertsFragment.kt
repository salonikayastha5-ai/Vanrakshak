package com.vanrakshak.app.ui.alerts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vanrakshak.app.R
import com.vanrakshak.app.data.local.UserSessionManager
import com.vanrakshak.app.databinding.FragmentAlertsBinding

/**
 * Interactive Alert Centre Fragment handling severity filtering,
 * alert acknowledgement, search, and threat inspection.
 */
class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        val userName = UserSessionManager(requireContext()).getUserName()

        // Search Filter
        binding.etSearchAlerts.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterAlertsByQuery(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Chip Filters
        binding.chipGroupAlerts.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedId = checkedIds.firstOrNull() ?: R.id.chipAlertsAll
            filterAlertsByChip(selectedId)
        }

        // Acknowledge Action 1
        binding.btnAlert1Ack.setOnClickListener {
            binding.tvAlert1AckStatus.text = "Timestamp: Today, 10:35 AM • 🟢 Acknowledged by $userName"
            binding.tvAlert1AckStatus.setTextColor(requireContext().getColor(R.color.accent_status_green))
            binding.btnAlert1Ack.visibility = View.GONE
            Snackbar.make(binding.root, "Alert ALT-2026-8801 acknowledged by $userName", Snackbar.LENGTH_LONG).show()
        }

        binding.btnAlert1Details.setOnClickListener {
            showAlertDetails("🚨 Possible Chainsaw Sound", "ALT-2026-8801", "Node #07 (Teak Reserve)", "Zone B-3", "94.2%", "Critical", "10:35 AM")
        }

        binding.btnAlert2Details.setOnClickListener {
            showAlertDetails("⚠️ Suspicious Mechanical Sound", "ALT-2026-8794", "Node #03 (River Basin)", "Zone B-1", "81.5%", "Warning", "09:12 AM")
        }

        // Acknowledge Action 3
        binding.btnAlert3Ack.setOnClickListener {
            binding.tvAlert3AckStatus.text = "Timestamp: Yesterday, 11:45 PM • 🟢 Acknowledged by $userName"
            binding.tvAlert3AckStatus.setTextColor(requireContext().getColor(R.color.accent_status_green))
            binding.btnAlert3Ack.visibility = View.GONE
            Snackbar.make(binding.root, "Alert ALT-2026-8780 acknowledged by $userName", Snackbar.LENGTH_LONG).show()
        }

        binding.btnAlert3Details.setOnClickListener {
            showAlertDetails("🚨 Gunshot-like Impulse Sound", "ALT-2026-8780", "Node #04 (Ridge Sector)", "Zone B-3", "88.7%", "Critical", "11:45 PM")
        }
    }

    private fun filterAlertsByQuery(query: String) {
        val q = query.lowercase().trim()
        val show1 = q.isEmpty() || "chainsaw".contains(q) || "node #07".contains(q) || "zone b-3".contains(q)
        val show2 = q.isEmpty() || "mechanical".contains(q) || "node #03".contains(q) || "zone b-1".contains(q)
        val show3 = q.isEmpty() || "gunshot".contains(q) || "node #04".contains(q) || "zone b-3".contains(q)

        binding.cardAlert1.visibility = if (show1) View.VISIBLE else View.GONE
        binding.cardAlert2.visibility = if (show2) View.VISIBLE else View.GONE
        binding.cardAlert3.visibility = if (show3) View.VISIBLE else View.GONE
    }

    private fun filterAlertsByChip(chipId: Int) {
        when (chipId) {
            R.id.chipAlertsCritical -> {
                binding.cardAlert1.visibility = View.VISIBLE
                binding.cardAlert2.visibility = View.GONE
                binding.cardAlert3.visibility = View.VISIBLE
            }
            R.id.chipAlertsUnack -> {
                binding.cardAlert1.visibility = if (binding.btnAlert1Ack.visibility == View.VISIBLE) View.VISIBLE else View.GONE
                binding.cardAlert2.visibility = View.GONE
                binding.cardAlert3.visibility = if (binding.btnAlert3Ack.visibility == View.VISIBLE) View.VISIBLE else View.GONE
            }
            else -> { // All
                binding.cardAlert1.visibility = View.VISIBLE
                binding.cardAlert2.visibility = View.VISIBLE
                binding.cardAlert3.visibility = View.VISIBLE
            }
        }
    }

    private fun showAlertDetails(event: String, alertId: String, node: String, zone: String, confidence: String, severity: String, time: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(event)
            .setMessage("Alert ID: $alertId\nHardware Node: $node\nForest Sector: $zone\nAI Threat Confidence: $confidence\nSeverity Level: $severity\nDetected Time: $time\n\nRecommendation: Dispatch Field Patrol to $zone immediately.")
            .setPositiveButton("Dispatch Patrol") { _, _ ->
                Snackbar.make(binding.root, "Forest Guard Patrol dispatched to $zone", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
