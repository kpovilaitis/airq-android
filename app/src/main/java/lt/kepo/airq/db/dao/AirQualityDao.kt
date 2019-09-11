package lt.kepo.airq.db.dao

import androidx.room.*
import lt.kepo.airq.db.model.AirQuality

@Dao
interface AirQualityDao {
    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    fun getByStationId(stationId: Int): AirQuality

    @Query("SELECT * FROM air_qualities WHERE is_current_location_quality = 1")
    fun getHere(): AirQuality

    @Transaction
    fun upsertHere(airQuality: AirQuality) {
        if (updateHere(
            airQuality.stationId,
            airQuality.airQualityIndex,
            airQuality.dominatingPollutant,
            airQuality.isCurrentLocationQuality,
            airQuality.city.name,
            airQuality.individualIndices.sulfurOxide.value,
            airQuality.individualIndices.ozone.value,
            airQuality.individualIndices.particle10.value,
            airQuality.individualIndices.particle25.value) == 0) upsert(airQuality)
    }

    @Query("UPDATE air_qualities " +
            "SET station_id = :stationId, " +
            "air_quality_index = :airQualityIndex, " +
            "dominating_pollutant = :dominatingPollutant, " +
            "is_current_location_quality = :isCurrentLocationQuality, " +
            "city_name = :cityName, " +
            "sulfur_oxide_value = :sulfurOxideValue, " +
            "ozone_value = :ozoneValue, " +
            "particle_10_value = :particle10Value, " +
            "particle_25_value = :particle25Value " +
            "WHERE is_current_location_quality = 1")
    fun updateHere(
        stationId: Int,
        airQualityIndex: Int,
        dominatingPollutant: String,
        isCurrentLocationQuality: Boolean,
        cityName: String,
        sulfurOxideValue: Double,
        ozoneValue: Double,
        particle10Value: Double,
        particle25Value: Double): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(airQuality: AirQuality): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(airQuality: AirQuality)

    @Query("DELETE FROM air_qualities WHERE station_id = :stationId")
    fun delete(stationId: Int)

    @Transaction
    fun upsert(airQuality: AirQuality) {
        if (insert(airQuality) == -1L) update(airQuality)
    }
}