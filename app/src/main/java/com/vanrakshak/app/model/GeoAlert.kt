package com.vanrakshak.app.model

/**
 * Shared Data Model for AI Acoustic Detection Alert.
 */
data class GeoAlert(
    val alertId: String,
    val nodeId: String,
    val event: String,
    val confidence: Double,
    val timestamp: String,
    val severity: String, // "CRITICAL", "WARNING"
    val latitude: Double,
    val longitude: Double,
    val status: String // "UNACKNOWLEDGED", "ACKNOWLEDGED"
)
