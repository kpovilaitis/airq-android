package lt.kepo.airq.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import lt.kepo.airq.db.model.AirQuality

@Dao
interface AirQualityDao {
    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    suspend fun getByStationId(stationId: Int): AirQuality

    @Query("SELECT * FROM air_qualities WHERE is_current_location_quality = 1")
    suspend fun getHere(): AirQuality

    @Transaction
    suspend fun upsertHere(airQuality: AirQuality) {
        if (updateHere(
                airQuality.stationId,
                airQuality.airQualityIndex,
                airQuality.dominatingPollutant,
                airQuality.isCurrentLocationQuality,
                airQuality.city.name,
                airQuality.time.localTime.time,
                airQuality.individualIndices.sulfurOxide.value,
                airQuality.individualIndices.ozone.value,
                airQuality.individualIndices.particle10.value,
                airQuality.individualIndices.particle25.value) == 0)
            upsert(airQuality)
    }

    @Query("UPDATE air_qualities " +
            "SET station_id = :stationId, " +
            "air_quality_index = :airQualityIndex, " +
            "dominating_pollutant = :dominatingPollutant, " +
            "is_current_location_quality = :isCurrentLocationQuality, " +
            "city_name = :cityName, " +
            "time_local = :localTime, " +
            "sulfur_oxide_value = :sulfurOxideValue, " +
            "ozone_value = :ozoneValue, " +
            "particle_10_value = :particle10Value, " +
            "particle_25_value = :particle25Value " +
            "WHERE is_current_location_quality = 1")
    suspend fun updateHere(
        stationId: Int,
        airQualityIndex: String,
        dominatingPollutant: String,
        isCurrentLocationQuality: Boolean,
        cityName: String,
        localTime: Long,
        sulfurOxideValue: Double,
        ozoneValue: Double,
        particle10Value: Double,
        particle25Value: Double): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(airQuality: AirQuality): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(airQuality: AirQuality)

    @Query("DELETE FROM air_qualities WHERE station_id = :stationId")
    suspend fun delete(stationId: Int)

    @Transaction
    suspend fun upsert(airQuality: AirQuality) {
        if (insert(airQuality) == -1L) update(airQuality)
    }
}