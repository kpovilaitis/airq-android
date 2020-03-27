package lt.kepo.database.typeconverter

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? =
        value?.run { Date(this) }

    @TypeConverter
    fun toLong(value: Date?): Long? =
        value?.time
}