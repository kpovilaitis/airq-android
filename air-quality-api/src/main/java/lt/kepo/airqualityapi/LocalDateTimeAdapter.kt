package lt.kepo.airqualityapi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class LocalDateTimeAdapter {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @ToJson
    fun toJson(value: Date): String {
        return formatter.format(value)
    }

    @FromJson
    fun fromJson(value: String): Date {
        return formatter.parse(value) ?: error("")
    }
}