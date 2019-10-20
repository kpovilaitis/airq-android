package lt.kepo.airq.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.db.dao.AirQualityDao
import lt.kepo.airq.data.db.dao.StationDao
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.db.typeconverter.DateTypeConverter

@TypeConverters(DateTypeConverter::class)
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
