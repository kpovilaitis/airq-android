package lt.kepo.airq.db.model

import androidx.room.ColumnInfo
import lt.kepo.airq.api.dto.TimeDto
import java.util.*

data class Time (
    @ColumnInfo(name = "local") val localTime: Date
) {
    companion object {
        fun build(dto: TimeDto): Time {
            return Time(
                dto.localTime
            )
        }
    }
}