package com.vanrakshak.app.model

/**
 * Shared Data Model for Forest Monitoring Hardware Node.
 */
data class GeoNode(
    val nodeId: String,
    val nodeName: String,
    val latitude: Double,
    val longitude: Double,
    val batteryPct: Int,
    val connectivityStatus: String, // "ONLINE", "LOW BATTERY", "OFFLINE"
    val lastSeen: String,
    val zoneId: String
)
