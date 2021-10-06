package lt.kepo.airqualitydata

data class AirQualityListItem(
    val stationId: Int,
    val address: String,
    val airQualityIndex: String,
    val isCurrentLocationQuality: Boolean,
)
