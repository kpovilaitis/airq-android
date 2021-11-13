package lt.kepo.airqualitydata

data class AirQualityListItem(
    val stationId: Int,
    val address: String,
    val index: String,
    val isCurrentLocation: Boolean,
)
