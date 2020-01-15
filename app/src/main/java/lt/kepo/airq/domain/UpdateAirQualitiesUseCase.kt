package lt.kepo.airq.domain

import lt.kepo.airq.data.api.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository

class UpdateAirQualitiesUseCase (private val airQualityRepository: AirQualityRepository) {
    suspend operator fun invoke(force: Boolean, qualities: List<AirQuality>): ApiResponse<List<AirQuality>> {
        val fetchedQualities = mutableListOf<AirQuality>()

        qualities.filter { force || it.shouldUpdate() }
            .forEach {
                when (val apiResponse = airQualityRepository.updateAirQuality(it.stationId)) {
                    is ApiSuccessResponse -> fetchedQualities.add(apiResponse.data)
                    is ApiErrorResponse -> return ApiErrorResponse(apiResponse.error)
                }
            }

        return ApiSuccessResponse(fetchedQualities)
    }
}