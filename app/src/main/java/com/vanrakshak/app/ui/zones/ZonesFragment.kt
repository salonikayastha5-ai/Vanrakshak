package com.vanrakshak.app.ui.zones

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vanrakshak.app.databinding.FragmentZonesBinding
import com.vanrakshak.app.ui.geomonitor.GeoMonitorActivity

/**
 * Forest Zone Management Fragment displaying sector coverage,
 * node counts, threat status, and sample geographic coordinates.
 */
class ZonesFragment : Fragment() {

    private var _binding: FragmentZonesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZonesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        // Search Filter
        binding.etSearchZones.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterZonesByQuery(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Zone 1
        binding.cardZone1.setOnClickListener { showZoneDetails("Zone A — North Canopy Reserve", "ZONE-A", "3 Active Nodes", "Normal", "21.1458° N, 79.0882° E") }
        binding.btnZone1Details.setOnClickListener { showZoneDetails("Zone A — North Canopy Reserve", "ZONE-A", "3 Active Nodes", "Normal", "21.1458° N, 79.0882° E") }

        // Zone 2
        binding.cardZone2.setOnClickListener { showZoneDetails("Zone B — Teak & Timber Sector", "ZONE-B", "4 Nodes (1 Offline)", "CRITICAL (Chainsaw Alert)", "21.1620° N, 79.1015° E") }
        binding.btnZone2Details.setOnClickListener { showZoneDetails("Zone B — Teak & Timber Sector", "ZONE-B", "4 Nodes (1 Offline)", "CRITICAL (Chainsaw Alert)", "21.1620° N, 79.1015° E") }

        // Zone 3
        binding.cardZone3.setOnClickListener { showZoneDetails("Zone C — Sanctuary Buffer", "ZONE-C", "3 Active Nodes", "Normal", "21.1310° N, 79.0740° E") }
        binding.btnZone3Details.setOnClickListener { showZoneDetails("Zone C — Sanctuary Buffer", "ZONE-C", "3 Active Nodes", "Normal", "21.1310° N, 79.0740° E") }
    }

    private fun filterZonesByQuery(query: String) {
        val q = query.lowercase().trim()
        val show1 = q.isEmpty() || "zone a".contains(q) || "north canopy".contains(q)
        val show2 = q.isEmpty() || "zone b".contains(q) || "teak".contains(q) || "timber".contains(q)
        val show3 = q.isEmpty() || "zone c".contains(q) || "sanctuary".contains(q)

        binding.cardZone1.visibility = if (show1) View.VISIBLE else View.GONE
        binding.cardZone2.visibility = if (show2) View.VISIBLE else View.GONE
        binding.cardZone3.visibility = if (show3) View.VISIBLE else View.GONE
    }

    private fun showZoneDetails(name: String, zoneId: String, nodeCount: String, status: String, coords: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(name)
            .setMessage("Zone ID: $zoneId\nCoverage: $nodeCount\nStatus: $status\nSample GPS Center: $coords\n\nInteractive Shared GeoMonitor Leaflet Map ready.")
            .setPositiveButton("Open GeoMonitor Map") { _, _ ->
                val intent = Intent(requireContext(), GeoMonitorActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
