package lt.kepo.airq.domain

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.api.mapOnSuccess
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.data.repository.stations.StationsRepository

class GetFilteredStationsUseCase (
    private val stationsRepository: StationsRepository,
    private val airQualityRepository: AirQualityRepository
) {
    suspend operator fun invoke(query: String): ApiResponse<MutableList<Station>> {
        return when (val response = stationsRepository.getRemoteStations(query)) {
            is ApiSuccessResponse -> response.mapOnSuccess {
                val airQualityHereStationId = airQualityRepository.getCachedAirQualityHereId()

                it.removeAll { item -> item.id == airQualityHereStationId }
                it
            }
            else -> response
        }
    }
}