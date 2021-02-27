package lt.kepo.stations

import lt.kepo.airqualityapi.AirQualityApi
import lt.kepo.airqualityapi.ApiResult
import lt.kepo.airqualityapi.call
import lt.kepo.airqualityapi.response.StationResponse
import javax.inject.Inject

class RemoteGetStationsUseCase @Inject constructor(
    private val airQualityApi: AirQualityApi
) : GetStationsUseCase {

    override suspend fun invoke(query: String): GetStationsUseCase.Result =
        airQualityApi.call {
            getStations(query)
        }.let { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> GetStationsUseCase.Result.Success(
                    stations = apiResult.data.map {
                        it.toDomainModel()
                    }
                )
                is ApiResult.Error -> GetStationsUseCase.Result.Error
            }
        }

    private fun StationResponse.toDomainModel(): Station =
        Station(
            id = id,
            airQualityIndex = airQualityIndex,
            name = station.name
        )
}