package lt.kepo.airq.db.dao

import androidx.room.*
import lt.kepo.airq.db.model.AirQuality

@Dao
interface AirQualityDao {
    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    fun getByStationId(stationId: Int): AirQuality

    @Query("SELECT * FROM air_qualities LIMIT 1")
    fun getHere(): AirQuality

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airQuality: AirQuality)

    @Query("DELETE FROM air_qualities")
    fun delete()

    @Transaction
    fun resetTable(airQuality: AirQuality) {
        delete()
        insert(airQuality)
    }
}