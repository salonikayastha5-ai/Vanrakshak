package com.vanrakshak.app.ui.geomonitor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.vanrakshak.app.databinding.ActivityGeomonitorBinding

/**
 * Android GeoMonitor Activity featuring Leaflet raster mapping,
 * custom User-Agent, primary Esri World Topo Map tiles with satellite fallback,
 * JavaScript Bridge for marker clicks, filter controls, and info panels.
 */
class GeoMonitorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeomonitorBinding
    private var selectedItemType: String = ""
    private var selectedItemId: String = ""
    private var selectedItemTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeomonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebViewMap()
        setupListeners()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebViewMap() {
        val webSettings: WebSettings = binding.wvGeoMonitorMap.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        // Configure standard Chrome mobile User-Agent string so tile servers recognize request
        webSettings.userAgentString = "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36 VanRakshakGeoMonitor/1.0"

        binding.wvGeoMonitorMap.webViewClient = WebViewClient()
        binding.wvGeoMonitorMap.addJavascriptInterface(WebAppInterface(), "AndroidBridge")
        binding.wvGeoMonitorMap.loadUrl("file:///android_asset/web/geomonitor.html")
    }

    private fun setupListeners() {
        binding.btnMapBack.setOnClickListener {
            finish()
        }

        binding.btnCloseMarkerInfo.setOnClickListener {
            binding.cardMarkerInfo.visibility = View.GONE
        }

        binding.btnMarkerAction.setOnClickListener {
            showMarkerDetailsDialog()
        }

        // Chip Filters
        binding.chipMapAll.setOnClickListener {
            binding.wvGeoMonitorMap.evaluateJavascript("filterMarkers('ALL')", null)
        }
        binding.chipMapNodes.setOnClickListener {
            binding.wvGeoMonitorMap.evaluateJavascript("filterMarkers('NODES')", null)
        }
        binding.chipMapAlerts.setOnClickListener {
            binding.wvGeoMonitorMap.evaluateJavascript("filterMarkers('ALERTS')", null)
        }
        binding.chipMapZones.setOnClickListener {
            binding.wvGeoMonitorMap.evaluateJavascript("filterMarkers('ZONES')", null)
        }
        binding.chipMapRecenter.setOnClickListener {
            binding.wvGeoMonitorMap.evaluateJavascript("resetCenter()", null)
            Snackbar.make(binding.root, "Map view centered to Nagzira Forest Sector", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showMarkerDetailsDialog() {
        val title = selectedItemTitle.ifBlank { "Item Details" }
        val message = when (selectedItemType) {
            "NODE" -> "Hardware Node ID: $selectedItemId\nLocation: Nagzira Forest Range\nStatus: Active Solar Node\nMicrophone: I2S MEMS Active"
            "ALERT" -> "Acoustic Threat ID: $selectedItemId\nEvent: $selectedItemTitle\nSeverity: Critical / High\nRecommendation: Dispatch Guard Patrol"
            "ZONE" -> "Conservation Zone ID: $selectedItemId\nSector: $selectedItemTitle\nNodes Assigned: Active Coverage"
            else -> "GeoMonitor Shared Dataset Item: $selectedItemId"
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onMarkerSelected(type: String, id: String, title: String) {
            runOnUiThread {
                selectedItemType = type
                selectedItemId = id
                selectedItemTitle = title

                binding.tvMarkerTitle.text = title
                binding.tvMarkerSubtitle.text = "Type: $type • ID: $id"
                binding.cardMarkerInfo.visibility = View.VISIBLE
            }
        }
    }
}
