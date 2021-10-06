package lt.kepo.airqualitynetwork

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class LocalDateTimeAdapter {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @ToJson
    fun toJson(value: Date?): String =
        value?.let { formatter.format(it) } ?: ""

    @FromJson
    fun fromJson(value: String): Date? =
        formatter.parse(value)
}