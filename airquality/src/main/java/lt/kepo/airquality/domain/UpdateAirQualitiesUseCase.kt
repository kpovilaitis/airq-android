package lt.kepo.airquality.domain

import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.core.model.AirQuality
import lt.kepo.core.network.ApiErrorResponse
import lt.kepo.core.network.ApiResponse
import lt.kepo.core.network.ApiSuccessResponse

class UpdateAirQualitiesUseCase (private val airQualityRepository: AirQualityRepository) {
    suspend operator fun invoke(force: Boolean, qualities: List<AirQuality>): ApiResponse<List<AirQuality>> {
        val fetchedQualities = mutableListOf<AirQuality>()

        qualities.filter { force || it.shouldUpdate() }
            .forEach {
                when (val response = airQualityRepository.updateAirQuality(it.stationId)) {
                    is ApiSuccessResponse -> fetchedQualities.add(response.data)
                    is ApiErrorResponse -> return ApiErrorResponse(
                        response.error
                    )
                }
            }

        return ApiSuccessResponse(fetchedQualities)
    }
}