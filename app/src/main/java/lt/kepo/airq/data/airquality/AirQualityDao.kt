package lt.kepo.airq.data.airquality

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.kepo.airq.data.models.AirQuality

@Dao
interface AirQualityDao {
    @Query("SELECT * FROM air_qualities WHERE id = :stationId")
    fun getByStationId(stationId: Int): LiveData<AirQuality>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airQuality: AirQuality)
}