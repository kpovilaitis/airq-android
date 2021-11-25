package lt.kepo.airqualitydata.refresh

import lt.kepo.airqualitydata.toEntityModel
import lt.kepo.airqualitynetwork.AirQualityApi
import lt.kepo.airqualitynetwork.ApiResult
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.location.LocationClient
import javax.inject.Inject

class RemoteRefreshAirQualityHereUseCase @Inject constructor(
    private val locationClient: LocationClient,
    private val airQualityApi: AirQualityApi,
    private val airQualityDao: AirQualityDao
) : RefreshAirQualityHereUseCase {

    override suspend fun invoke(): RefreshAirQualityHereUseCase.Result =
        when (val location = locationClient.getLastLocation()) {
            null -> airQualityApi.getAirQualityHere()
            else -> airQualityApi.getAirQualityHere(
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString()
            )
        }.let { airQualityResponse ->
            when (airQualityResponse) {
                is ApiResult.Success -> {
                    airQualityDao.run {
                        deleteCurrentLocation()
                        insert(
                            airQuality = airQualityResponse.data.toEntityModel(
                                isCurrentLocationQuality = true,
                            )
                        )
                    }
                    RefreshAirQualityHereUseCase.Result.Success
                }
                is ApiResult.Error -> {
                    RefreshAirQualityHereUseCase.Result.Error
                }
            }
        }
}
