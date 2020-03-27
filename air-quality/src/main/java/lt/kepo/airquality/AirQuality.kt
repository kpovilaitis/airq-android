package lt.kepo.airquality

import java.util.*

data class AirQuality(
    val stationId: Int,
    val primaryAddress: String,
    val secondaryAddress: String,
    val airQualityIndex: String,
    val sulfurOxide: Double?,
    val ozone: Double?,
    val particle10: Double?,
    val particle25: Double?,
    val isCurrentLocationQuality: Boolean,
    val localTimeRecorded: Date,
)
