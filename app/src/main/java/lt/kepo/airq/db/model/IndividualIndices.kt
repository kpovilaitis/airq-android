package lt.kepo.airq.db.model

import androidx.room.Embedded
import lt.kepo.airq.api.dto.IndividualIndicesDto

data class IndividualIndices(
    @Embedded(prefix = "so2_") val sulfurOxide: Property,
    @Embedded(prefix = "o3_") val ozone: Property,
    @Embedded(prefix = "pm10_") val particle10: Property,
    @Embedded(prefix = "pm25_") val particle25: Property
) {
    companion object {
        fun build(dto: IndividualIndicesDto): IndividualIndices {
            return IndividualIndices(
                Property.build(dto.sulfurOxide),
                Property.build(dto.ozone),
                Property.build(dto.particle10),
                Property.build(dto.particle25)
            )
        }
    }
}