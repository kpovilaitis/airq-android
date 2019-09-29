package lt.kepo.airq.db

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.db.dao.AirQualityDao
import lt.kepo.airq.db.dao.StationDao
import lt.kepo.airq.db.model.Station

@Database(
    entities = [
        AirQuality::class,
        Station::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun airQualityDao(): AirQualityDao

    abstract fun stationDao(): StationDao
}
