package lt.kepo.airq.db.model

import lt.kepo.airq.api.dto.CityDto
import lt.kepo.airq.db.Builder

data class City (
    val name: String
) {
    companion object : Builder<CityDto, City> {
        override fun build(dto: CityDto): City {
            return City(
                dto.name
            )
        }
    }
}