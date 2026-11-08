package com.vanrakshak.app.model

/**
 * Data model matching ESP32-S3 & LoRa gateway acoustic telemetry packet.
 * Prepared for future hardware integration.
 */
data class HardwarePacketModel(
    val nodeId: String,
    val event: String,
    val confidence: Double,
    val timestamp: Long,
    val batteryPct: Int,
    val approximateZone: String,
    val sequence: Long
)
