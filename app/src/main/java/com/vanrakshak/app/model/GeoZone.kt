package com.vanrakshak.app.model

/**
 * Shared Data Model for Forest Monitoring Conservation Zone.
 */
data class GeoZone(
    val zoneId: String,
    val zoneName: String,
    val boundaryCoordinates: List<Pair<Double, Double>>,
    val description: String,
    val status: String // "NORMAL", "ALERT ACTIVE", "MAINTENANCE"
)
