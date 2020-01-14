package lt.kepo.airq.domain

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.data.api.mapOnSuccess
import lt.kepo.airq.data.model.Station
import java.lang.Exception

class GetFilteredStationsUseCase constructor(
    private val httpClient: HttpClient
) {
    suspend operator fun invoke(query: String, stationId: Int): ApiResponse<MutableList<Station>> {
        return try {
            ApiResponse.parse(httpClient.getStations(query)).mapOnSuccess { data ->
                data.removeAll { item -> item.id == stationId }
                data
            }
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }
}