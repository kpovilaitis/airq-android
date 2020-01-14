package lt.kepo.airq.domain

import android.location.Location
import lt.kepo.airq.data.api.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import java.lang.Exception

class UpdateAirQualityHereUseCase constructor(
    private val httpClient: HttpClient,
    private val airQualityRepository: AirQualityRepository
) {
    suspend operator fun invoke(
        force: Boolean,
        airQualityHere: AirQuality?,
        location: Location?
    ): ApiResponse<AirQuality> {
        return try {
            if (force || airQualityHere == null || airQualityHere.shouldUpdate()) {
                val response = if (location != null) {
                    httpClient.getAirQualityHere("geo:${location.latitude};${location.longitude}")
                } else {
                    httpClient.getAirQualityHere()
                }

                ApiResponse.parse(response).mapOnSuccess {
                    it.isCurrentLocationQuality = true

                    airQualityRepository.deleteLocalAirQualityHere()
                    airQualityRepository.insertLocalAirQuality(it)

                    it
                }
            } else {
                ApiSuccessResponse(airQualityHere)
            }
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }
}