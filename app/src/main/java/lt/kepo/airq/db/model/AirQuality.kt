package lt.kepo.airq.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.kepo.airq.api.dto.AirQualityDto

@Entity(tableName = "air_qualities")
data class AirQuality (
    @PrimaryKey @ColumnInfo(name = "station_id") val stationId: Int,
    @ColumnInfo(name = "air_quality_index") val airQualityIndex: Int,
    @ColumnInfo(name = "dominating_pollutant") val dominatingPollutant: String,
    @Embedded val individualIndices: IndividualIndices,
    @Embedded val city: City
) {
    companion object {
        fun build(dto: AirQualityDto): AirQuality {
            return AirQuality(
                dto.stationId,
                dto.airQualityIndex,
                dto.dominatingPollutant,
                IndividualIndices.build(dto.individualIndices),
                City.build(dto.city)
            )
        }
    }
}