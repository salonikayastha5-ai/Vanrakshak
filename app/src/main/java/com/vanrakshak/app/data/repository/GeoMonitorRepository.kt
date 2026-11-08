package com.vanrakshak.app.data.repository

import com.vanrakshak.app.model.GeoAlert
import com.vanrakshak.app.model.GeoNode
import com.vanrakshak.app.model.GeoZone

/**
 * Shared Data Architecture Repository providing consistent sample dataset
 * for both Android GeoMonitor screen and Web Leaflet platform.
 */
object GeoMonitorRepository {

    const val FOREST_CENTER_LAT = 21.1458
    const val FOREST_CENTER_LNG = 79.0882
    const val IS_DEMO_MODE = true

    fun getSampleZones(): List<GeoZone> {
        return listOf(
            GeoZone(
                zoneId = "ZONE-A",
                zoneName = "Zone A — North Canopy Reserve",
                boundaryCoordinates = listOf(
                    Pair(21.1550, 79.0820),
                    Pair(21.1620, 79.0820),
                    Pair(21.1620, 79.0950),
                    Pair(21.1550, 79.0950)
                ),
                description = "Dense evergreen canopy reserve with high-density acoustic coverage.",
                status = "NORMAL"
            ),
            GeoZone(
                zoneId = "ZONE-B",
                zoneName = "Zone B — Teak & Timber Sector",
                boundaryCoordinates = listOf(
                    Pair(21.1420, 79.0920),
                    Pair(21.1510, 79.0920),
                    Pair(21.1510, 79.1080),
                    Pair(21.1420, 79.1080)
                ),
                description = "High value teak timber zone. Active illegal logging threat sector.",
                status = "ALERT ACTIVE"
            ),
            GeoZone(
                zoneId = "ZONE-C",
                zoneName = "Zone C — Sanctuary Buffer",
                boundaryCoordinates = listOf(
                    Pair(21.1300, 79.0720),
                    Pair(21.1400, 79.0720),
                    Pair(21.1400, 79.0850),
                    Pair(21.1300, 79.0850)
                ),
                description = "Wildlife sanctuary buffer zone with active acoustic nodes.",
                status = "NORMAL"
            )
        )
    }

    fun getSampleNodes(): List<GeoNode> {
        return listOf(
            GeoNode("ND-01", "Node #01 — North Canopy", 21.1580, 79.0860, 92, "ONLINE", "2 mins ago", "ZONE-A"),
            GeoNode("ND-03", "Node #03 — River Basin Edge", 21.1320, 79.0980, 15, "LOW BATTERY", "15 mins ago", "ZONE-C"),
            GeoNode("ND-04", "Node #04 — Ridge Sector", 21.1470, 79.1020, 0, "OFFLINE", "4 hours ago", "ZONE-B"),
            GeoNode("ND-07", "Node #07 — Teak Reserve Core", 21.1450, 79.0960, 88, "ONLINE", "1 min ago", "ZONE-B")
        )
    }

    fun getSampleAlerts(): List<GeoAlert> {
        return listOf(
            GeoAlert("ALT-8801", "ND-07", "🚨 Possible Chainsaw Sound", 94.2, "Today, 10:35 AM", "CRITICAL", 21.1452, 79.0964, "UNACKNOWLEDGED"),
            GeoAlert("ALT-8794", "ND-03", "⚠️ Suspicious Mechanical Sound", 81.5, "Today, 09:12 AM", "WARNING", 21.1325, 79.0982, "ACKNOWLEDGED"),
            GeoAlert("ALT-8780", "ND-04", "🚨 Gunshot-like Impulse Sound", 88.7, "Yesterday, 11:45 PM", "CRITICAL", 21.1475, 79.1025, "UNACKNOWLEDGED")
        )
    }
}
