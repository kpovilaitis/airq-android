package lt.kepo.stations.repository

import lt.kepo.core.network.ApiResponse
import lt.kepo.core.model.AirQuality
import lt.kepo.core.model.Station

interface StationsRepository {
    suspend fun getRemoteStations(query: String): ApiResponse<MutableList<Station>>

    suspend fun addAirQuality(stationId: Int): ApiResponse<AirQuality>
}