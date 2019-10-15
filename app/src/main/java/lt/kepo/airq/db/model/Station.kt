package lt.kepo.airq.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.api.dto.PropertyDto
import lt.kepo.airq.api.dto.StationDto
import lt.kepo.airq.db.Builder

@Entity(tableName = "stations")
data class Station (
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "air_quality_index") var airQualityIndex: String,
    @Embedded(prefix = "city_") var station: City
)  {
    companion object : Builder<StationDto, Station> {
        override fun build(dto: StationDto): Station {
            return Station(
                dto.id,
                dto.airQualityIndex,
                City.build(dto.station)
            )
        }

        fun build(dto: AirQualityDto): Station {
            return Station(
                dto.stationId,
                dto.airQualityIndex,
                City.build(dto.city)
            )
        }
    }
}