package lt.kepo.airq.db.model

data class Station (
    val id: Int,
    val airQualityIndex: Int,
    var station: City
)