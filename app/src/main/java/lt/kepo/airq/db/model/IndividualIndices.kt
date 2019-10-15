package lt.kepo.airq.db.model

import androidx.room.Embedded
import lt.kepo.airq.api.dto.IndividualIndicesDto
import lt.kepo.airq.db.Builder

data class IndividualIndices (
    @Embedded(prefix = "sulfur_oxide_") val sulfurOxide: Property,
    @Embedded(prefix = "ozone_") val ozone: Property,
    @Embedded(prefix = "particle_10_") val particle10: Property,
    @Embedded(prefix = "particle_25_") val particle25: Property
) {
    companion object : Builder<IndividualIndicesDto, IndividualIndices> {
        override fun build(dto: IndividualIndicesDto): IndividualIndices {
            return IndividualIndices(
                Property.build(dto.sulfurOxide),
                Property.build(dto.ozone),
                Property.build(dto.particle10),
                Property.build(dto.particle25)
            )
        }
    }
}