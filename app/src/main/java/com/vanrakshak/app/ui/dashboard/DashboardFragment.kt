package com.vanrakshak.app.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vanrakshak.app.R
import com.vanrakshak.app.data.local.UserSessionManager
import com.vanrakshak.app.databinding.FragmentDashboardBinding
import com.vanrakshak.app.ui.geomonitor.GeoMonitorActivity
import com.vanrakshak.app.ui.main.MainActivity

/**
 * Advanced Interactive Dashboard Fragment displaying real-time system metrics,
 * quick action chips, interactive cards, notification bells, and activity logs.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val sessionManager = UserSessionManager(context)

        binding.tvDashWelcome.text = "Welcome, ${sessionManager.getUserName()}"
        binding.tvDashRole.text = sessionManager.getUserRole()

        setupInteractions()
    }

    private fun setupInteractions() {
        val mainActivity = activity as? MainActivity

        // Open GeoMonitor Map on Mode Pill Click
        binding.tvMonitoringModePill.setOnClickListener {
            val intent = Intent(requireContext(), GeoMonitorActivity::class.java)
            startActivity(intent)
        }

        // Notification Bell Click
        binding.btnNotificationBell.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("System Notifications (3)")
                .setMessage("1. 🚨 Chainsaw detection in Zone B-3 (94% confidence)\n\n2. ⚠️ Low battery on Node #03 (15% solar charge)\n\n3. ℹ️ Daily acoustic heartbeat summary ready")
                .setPositiveButton("View All Alerts") { _, _ ->
                    mainActivity?.selectNavigationTab(R.id.nav_alerts)
                }
                .setNegativeButton("Close", null)
                .show()
        }

        // Quick Action Chips
        binding.chipActionNodes.setOnClickListener {
            mainActivity?.selectNavigationTab(R.id.nav_nodes)
        }

        binding.chipActionAlerts.setOnClickListener {
            mainActivity?.selectNavigationTab(R.id.nav_alerts)
        }

        binding.chipActionZones.setOnClickListener {
            val intent = Intent(requireContext(), GeoMonitorActivity::class.java)
            startActivity(intent)
        }

        binding.chipActionReports.setOnClickListener {
            showSystemReportDialog()
        }

        // Interactive Statistics Cards
        binding.cardNodesStat.setOnClickListener {
            mainActivity?.selectNavigationTab(R.id.nav_nodes)
        }

        binding.cardAlertsStat.setOnClickListener {
            mainActivity?.selectNavigationTab(R.id.nav_alerts)
        }

        binding.cardConnectivity.setOnClickListener {
            Snackbar.make(binding.root, "Network Health: 83.3% online across 4 forest sectors", Snackbar.LENGTH_SHORT).show()
        }

        binding.cardBatteryHealth.setOnClickListener {
            Snackbar.make(binding.root, "Solar Battery Health: Avg 87%. 1 node requires charging.", Snackbar.LENGTH_SHORT).show()
        }

        binding.cardWeeklyActivity.setOnClickListener {
            Snackbar.make(binding.root, "Weekly Summary: Peak acoustic events recorded on Thursday (48 detections)", Snackbar.LENGTH_LONG).show()
        }

        // Recent Activity Items
        binding.itemActivity1.setOnClickListener {
            showActivityDetailsDialog(
                title = "🚨 Chainsaw Detection Event",
                details = "Event ID: ALT-2026-8801\nNode: Node #07 (Teak Reserve)\nZone: Zone B-3\nConfidence: 94.2%\nTimestamp: 12 mins ago\nStatus: Unacknowledged"
            )
        }

        binding.itemActivity2.setOnClickListener {
            showActivityDetailsDialog(
                title = "⚠️ Low Battery Warning",
                details = "Node: Node #03 (River Basin Edge)\nZone: Zone B-1\nCurrent Charge: 15%\nEstimated Runtime: 14 Hours\nRecommendation: Inspect solar panel connection"
            )
        }

        binding.itemActivity3.setOnClickListener {
            showActivityDetailsDialog(
                title = "🟢 Acoustic Heartbeat Received",
                details = "Node: Node #12 (Border Range)\nZone: Zone D-1\nSignal Strength: -68 dBm\nStatus: Healthy & Active"
            )
        }
    }

    private fun showSystemReportDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("VanRakshak System Intelligence Report")
            .setMessage("Forest Sector: Division North & Teak Reserve\nActive Hardware Nodes: 10 / 12\nTotal Detections Today: 8\nChainsaw Threats: 1 Critical\nSystem Uptime: 99.4%\nData Mode: Local Persistence & Demo Simulation")
            .setPositiveButton("Export PDF Summary") { _, _ ->
                Snackbar.make(binding.root, "Demo Mode: System report exported to local storage", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Dismiss", null)
            .show()
    }

    private fun showActivityDetailsDialog(title: String, details: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(details)
            .setPositiveButton("View in Alerts") { _, _ ->
                (activity as? MainActivity)?.selectNavigationTab(R.id.nav_alerts)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
