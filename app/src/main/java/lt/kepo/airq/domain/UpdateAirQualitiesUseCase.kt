package lt.kepo.airq.domain

import lt.kepo.airq.data.api.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import java.lang.Exception

class UpdateAirQualitiesUseCase constructor(
    private val httpClient: HttpClient,
    private val airQualityRepository: AirQualityRepository
) {
    suspend operator fun invoke(force: Boolean, qualities: List<AirQuality>): ApiResponse<List<AirQuality>> {
        return try {
            ApiSuccessResponse(updateAvailable(force, qualities))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }

    private suspend fun updateAvailable(force: Boolean, qualities: List<AirQuality>): List<AirQuality> {
        val fetchedQualities = mutableListOf<AirQuality>()

        qualities.filter { force || it.shouldUpdate() }
            .forEach {
                ApiResponse.parse(httpClient.getStation("@${it.stationId}"))
                    .mapOnSuccess { data ->
                        airQualityRepository.insertLocalAirQuality(data)
                        fetchedQualities.add(data)
                        data
                    }
            }

        return fetchedQualities
    }
}