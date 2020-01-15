package lt.kepo.airq.domain

import android.location.Location
import lt.kepo.airq.data.api.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository

class UpdateAirQualityHereUseCase (private val airQualityRepository: AirQualityRepository) {
    suspend operator fun invoke(
        force: Boolean,
        airQualityHere: AirQuality?,
        location: Location?
    ): ApiResponse<AirQuality> {
        return if (force || airQualityHere == null || airQualityHere.shouldUpdate()) {
            airQualityRepository.updateAirQualityHere(location)
        } else {
            ApiSuccessResponse(airQualityHere)
        }
    }
}