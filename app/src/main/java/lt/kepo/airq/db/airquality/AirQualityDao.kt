package lt.kepo.airq.db.airquality

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.kepo.airq.data.models.AirQuality

@Dao
interface AirQualityDao {
    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    fun getByStationId(stationId: Int): LiveData<AirQuality>

    @Query("SELECT * FROM air_qualities LIMIT 1")
    fun getHere(): AirQuality

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airQuality: AirQuality)
}