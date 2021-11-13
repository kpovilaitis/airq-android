package lt.kepo.airqualitydata.refresh

import lt.kepo.airqualitydata.toEntityModel
import lt.kepo.airqualitynetwork.AirQualityApi
import lt.kepo.airqualitynetwork.ApiResult
import lt.kepo.airqualitynetwork.call
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Inject

class RemoteRefreshAirQualityUseCase @Inject constructor(
    private val airQualityApi: AirQualityApi,
    private val airQualityDao: AirQualityDao
) : RefreshAirQualityUseCase {

    override suspend fun invoke(
        stationId: Int
    ): RefreshAirQualityUseCase.Result =
        airQualityApi.call {
            getAirQuality(
                stationId = stationId
            )
        }.let { airQualityResponse ->
            when (airQualityResponse) {
                is ApiResult.Success -> {
                    airQualityDao.insert(
                        airQuality = airQualityResponse.data.toEntityModel(
                            isCurrentLocationQuality = false
                        )
                    )
                    RefreshAirQualityUseCase.Result.Success
                }
                is ApiResult.Error -> {
                    RefreshAirQualityUseCase.Result.Error
                }
            }
        }
}
