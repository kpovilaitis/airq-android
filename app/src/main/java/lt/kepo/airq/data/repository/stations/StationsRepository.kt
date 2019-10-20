package lt.kepo.airq.data.repository.stations

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.model.Station

interface StationsRepository {
    suspend fun getRemoteStations(query: String): ApiResponse<MutableList<Station>>

    suspend fun getLocalAllStations(): MutableList<Station>

    suspend fun getRemoteStation(stationId: Int): ApiResponse<AirQuality>

    suspend fun upsertLocalStation(station: Station)

    suspend fun updateLocalStation(station: Station)

    suspend fun deleteLocalStation(station: Station)
}