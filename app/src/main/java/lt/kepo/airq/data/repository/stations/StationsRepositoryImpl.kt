package lt.kepo.airq.data.repository.stations

import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.data.model.Station
import java.lang.Exception

class StationsRepositoryImpl internal constructor(private val httpClient: HttpClient) : StationsRepository {
    override suspend fun getRemoteStations(query: String): ApiResponse<MutableList<Station>> {
        return try {
            ApiResponse.parse(httpClient.getStations(query))
        } catch (exception: Exception) {
            ApiResponse.parse(exception)
        }
    }
}