package lt.kepo.airq.db

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.kepo.airq.data.models.AirQuality
import lt.kepo.airq.db.airquality.AirQualityDao

@Database(
    entities = [
        AirQuality::class
    ],
    version = 1
)
abstract class AirQualityDatabase : RoomDatabase() {

    abstract fun airQualityDao(): AirQualityDao
}
