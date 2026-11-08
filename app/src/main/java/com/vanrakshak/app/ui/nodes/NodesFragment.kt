package com.vanrakshak.app.ui.nodes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vanrakshak.app.R
import com.vanrakshak.app.databinding.FragmentNodesBinding

/**
 * Interactive Node Management Fragment supporting search, filtering,
 * node details view, demo node creation, and deletion confirmation dialogs.
 */
class NodesFragment : Fragment() {

    private var _binding: FragmentNodesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        // Add Node Action
        binding.btnAddDemoNode.setOnClickListener {
            showAddNodeDialog()
        }

        // Search Filter Listener
        binding.etSearchNodes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNodesByQuery(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Chip Filters
        binding.chipGroupNodes.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedId = checkedIds.firstOrNull() ?: R.id.chipFilterAll
            filterNodesByChip(selectedId)
        }

        // Card & Details Clicks
        binding.cardNode1.setOnClickListener { showNodeDetails("Node #01 — North Canopy", "Zone A-1", "92%", "🟢 ONLINE", "-65 dBm") }
        binding.btnNode1Details.setOnClickListener { showNodeDetails("Node #01 — North Canopy", "Zone A-1", "92%", "🟢 ONLINE", "-65 dBm") }
        binding.btnNode1Delete.setOnClickListener { confirmDeleteNode("Node #01", binding.cardNode1) }

        binding.cardNode2.setOnClickListener { showNodeDetails("Node #03 — River Basin Edge", "Zone B-1", "15%", "⚠️ LOW BATTERY", "-82 dBm") }
        binding.btnNode2Details.setOnClickListener { showNodeDetails("Node #03 — River Basin Edge", "Zone B-1", "15%", "⚠️ LOW BATTERY", "-82 dBm") }
        binding.btnNode2Delete.setOnClickListener { confirmDeleteNode("Node #03", binding.cardNode2) }

        binding.cardNode3.setOnClickListener { showNodeDetails("Node #04 — Ridge Sector", "Zone B-3", "0%", "🔴 OFFLINE", "Disconnected") }
        binding.btnNode3Details.setOnClickListener { showNodeDetails("Node #04 — Ridge Sector", "Zone B-3", "0%", "🔴 OFFLINE", "Disconnected") }
        binding.btnNode3Delete.setOnClickListener { confirmDeleteNode("Node #04", binding.cardNode3) }
    }

    private fun filterNodesByQuery(query: String) {
        val q = query.lowercase().trim()
        val show1 = q.isEmpty() || "node #01".contains(q) || "north canopy".contains(q) || "zone a-1".contains(q)
        val show2 = q.isEmpty() || "node #03".contains(q) || "river basin".contains(q) || "zone b-1".contains(q)
        val show3 = q.isEmpty() || "node #04".contains(q) || "ridge sector".contains(q) || "zone b-3".contains(q)

        binding.cardNode1.visibility = if (show1) View.VISIBLE else View.GONE
        binding.cardNode2.visibility = if (show2) View.VISIBLE else View.GONE
        binding.cardNode3.visibility = if (show3) View.VISIBLE else View.GONE
    }

    private fun filterNodesByChip(chipId: Int) {
        when (chipId) {
            R.id.chipFilterOnline -> {
                binding.cardNode1.visibility = View.VISIBLE
                binding.cardNode2.visibility = View.GONE
                binding.cardNode3.visibility = View.GONE
            }
            R.id.chipFilterLowBattery -> {
                binding.cardNode1.visibility = View.GONE
                binding.cardNode2.visibility = View.VISIBLE
                binding.cardNode3.visibility = View.GONE
            }
            R.id.chipFilterOffline -> {
                binding.cardNode1.visibility = View.GONE
                binding.cardNode2.visibility = View.GONE
                binding.cardNode3.visibility = View.VISIBLE
            }
            else -> { // All
                binding.cardNode1.visibility = View.VISIBLE
                binding.cardNode2.visibility = View.VISIBLE
                binding.cardNode3.visibility = View.VISIBLE
            }
        }
    }

    private fun showNodeDetails(name: String, zone: String, battery: String, status: String, signal: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Node Intelligence & Health")
            .setMessage("Node Name: $name\nForest Zone: $zone\nStatus: $status\nBattery: $battery Solar\nSignal Strength: $signal\nMicrophone: I2S MEMS Active\nAI Inference: TinyML Model v1.2 Active\n\nRecent Detection: No active chainsaw signature in last 24h.")
            .setPositiveButton("Edit Node") { _, _ ->
                Snackbar.make(binding.root, "Demo Mode: Node parameters updated", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun confirmDeleteNode(nodeName: String, cardView: View) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to remove $nodeName from the active monitoring system?")
            .setPositiveButton("Delete") { _, _ ->
                cardView.visibility = View.GONE
                Snackbar.make(binding.root, "$nodeName removed from monitoring list", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddNodeDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Demonstration Node")

        val inputName = EditText(requireContext()).apply { hint = "Node Name (e.g. Node #13 - Valley Ridge)" }
        val inputZone = EditText(requireContext()).apply { hint = "Forest Zone (e.g. Zone D-2)" }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 20)
            addView(inputName)
            addView(inputZone)
        }

        builder.setView(container)
            .setPositiveButton("Add Node") { _, _ ->
                val name = inputName.text.toString().ifBlank { "Node #13 — Demo Canopy" }
                val zone = inputZone.text.toString().ifBlank { "Zone D-2" }
                Snackbar.make(binding.root, "New node '$name' registered under $zone", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
