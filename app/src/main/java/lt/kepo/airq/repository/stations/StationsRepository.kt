package lt.kepo.airq.repository.stations

import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.api.dto.StationDto
import lt.kepo.airq.db.model.Station

interface StationsRepository {
    suspend fun getRemoteStations(query: String): ApiResponse<List<StationDto>>

    suspend fun getLocalAllStations(): List<Station>

    suspend fun getRemoteStation(stationId: Int): ApiResponse<AirQualityDto>

    suspend fun insertLocalStation(station: Station)

    suspend fun deleteLocalStation(station: Station)
}