package lt.kepo.airq.db.model

import lt.kepo.airq.api.dto.CityDto

data class City (
    val name: String
) {
    companion object {
        fun build(dto: CityDto): City {
            return City(
                dto.name
            )
        }
    }
}