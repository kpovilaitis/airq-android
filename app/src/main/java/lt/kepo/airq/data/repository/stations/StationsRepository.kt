package lt.kepo.airq.data.repository.stations

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.model.Station

interface StationsRepository {
    suspend fun getRemoteStations(query: String): ApiResponse<MutableList<Station>>

    suspend fun addAirQuality(stationId: Int): ApiResponse<AirQuality>
}