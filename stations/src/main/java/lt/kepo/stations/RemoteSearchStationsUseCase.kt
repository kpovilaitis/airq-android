package lt.kepo.stations

import kotlinx.coroutines.flow.first
import lt.kepo.airqualityapi.AirQualityApi
import lt.kepo.airqualityapi.ApiResult
import lt.kepo.airqualityapi.call
import lt.kepo.airqualityapi.response.StationResponse
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Inject

class RemoteSearchStationsUseCase @Inject constructor(
    private val airQualityApi: AirQualityApi,
    private val airQualityDao: AirQualityDao,
) : SearchStationsUseCase {

    override suspend fun invoke(query: String): SearchStationsUseCase.Result =
        airQualityApi.call {
            getStations(query)
        }.let { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> SearchStationsUseCase.Result.Success(
                    stations = apiResult.data
                        .filterNot { station ->
                            airQualityDao.getAll().first()
                                .map { it.stationId }
                                .contains(station.id)
                        }.take(10)
                        .map {
                            it.toDomainModel()
                        }
                )
                is ApiResult.Error -> SearchStationsUseCase.Result.Error
            }
        }

    private fun StationResponse.toDomainModel(): Station =
        Station(
            id = id,
            airQualityIndex = airQualityIndex,
            name = station.name
        )
}