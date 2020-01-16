package lt.kepo.airq.domain

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.api.mapOnSuccess
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.data.repository.stations.StationsRepository
import lt.kepo.airq.utility.AIR_QUALITY_HERE_STATION_ID

class GetFilteredStationsUseCase (private val stationsRepository: StationsRepository) {
    suspend operator fun invoke(query: String): ApiResponse<MutableList<Station>> {
        return when (val response = stationsRepository.getRemoteStations(query)) {
            is ApiSuccessResponse -> response.mapOnSuccess {
                it.removeAll { item -> item.id == AIR_QUALITY_HERE_STATION_ID }
                it
            }
            else -> response
        }
    }
}