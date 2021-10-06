package lt.kepo.airquality

import lt.kepo.airqualityapi.AirQualityApi
import lt.kepo.airqualityapi.ApiResult
import lt.kepo.airqualityapi.call
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.location.LocationClient
import javax.inject.Inject

class RemoteRefreshAirQualityHereUseCase @Inject constructor(
    private val locationClient: LocationClient,
    private val airQualityApi: AirQualityApi,
    private val airQualityDao: AirQualityDao
) : RefreshAirQualityHereUseCase {

    override suspend fun invoke(): RefreshAirQualityHereUseCase.Result =
        airQualityApi.call {
            when (val location = locationClient.getLastLocation()) {
                null -> getAirQualityHere()
                else -> getAirQualityHere(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString()
                )
            }
        }.also { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> {
                    airQualityDao.run {
                        deleteCurrentLocation()
                        insert(
                            airQuality = apiResult.data.toEntityModel(
                                isCurrentLocationQuality = true
                            )
                        )
                    }
                }
            }
        }.let { airQualityResponse ->
            when (airQualityResponse) {
                is ApiResult.Success -> RefreshAirQualityHereUseCase.Result.Success
                is ApiResult.Error -> RefreshAirQualityHereUseCase.Result.Error
            }
        }
}
