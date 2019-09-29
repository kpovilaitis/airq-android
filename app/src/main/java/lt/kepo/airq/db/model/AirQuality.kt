package lt.kepo.airq.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.kepo.airq.api.dto.AirQualityDto

@Entity(tableName = "air_qualities")
data class AirQuality (
    @PrimaryKey @ColumnInfo(name = "station_id") val stationId: Int,
    @ColumnInfo(name = "air_quality_index") val airQualityIndex: String,
    @ColumnInfo(name = "dominating_pollutant") val dominatingPollutant: String,
    @Embedded val individualIndices: IndividualIndices,
    @Embedded(prefix = "city_") val city: City,
    @ColumnInfo(name = "is_current_location_quality") var isCurrentLocationQuality: Boolean
) {
    companion object {
        fun build(dto: AirQualityDto): AirQuality {
            return AirQuality(
                dto.stationId,
                dto.airQualityIndex,
                dto.dominatingPollutant,
                IndividualIndices.build(dto.individualIndices),
                City.build(dto.city),
                dto.isCurrentLocationQuality
            )
        }
    }
}