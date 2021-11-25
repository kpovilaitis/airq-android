package lt.kepo.airqualitydata.search

import lt.kepo.airqualitydata.AirQualityListItem
import lt.kepo.airqualitynetwork.AirQualityApi
import lt.kepo.airqualitynetwork.ApiResult
import lt.kepo.airqualitynetwork.response.StationResponse
import javax.inject.Inject

class RemoteSearchAirQualitiesUseCase @Inject constructor(
    private val airQualityApi: AirQualityApi,
) : SearchAirQualitiesUseCase {

    override suspend fun invoke(query: String): SearchAirQualitiesUseCase.Result =
        airQualityApi.getStations(
            query = query,
        ).let { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> SearchAirQualitiesUseCase.Result.Success(
                    airQualities = apiResult.data
                        .take(10)
                        .map {
                            it.toDomainModel()
                        }
                )
                is ApiResult.Error -> SearchAirQualitiesUseCase.Result.Error
            }
        }

    private fun StationResponse.toDomainModel(): AirQualityListItem =
        AirQualityListItem(
            stationId = data.id,
            index = data.airQualityIndex,
            isCurrentLocation = false,
            address = data.station.name,
        )
}
