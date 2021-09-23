package lt.kepo.stations

import lt.kepo.airqualityapi.AirQualityApi
import lt.kepo.airqualityapi.ApiResult
import lt.kepo.airqualityapi.call
import lt.kepo.airqualityapi.response.AirQualityResponse
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.airqualitydatabase.AirQualityEntity
import javax.inject.Inject

class RemoteSaveStationUseCase @Inject constructor(
    private val airQualityDao: AirQualityDao,
    private val airQualityApi: AirQualityApi
) : SaveStationUseCase {

    override suspend operator fun invoke(stationId: Int): SaveStationUseCase.Result =
        airQualityApi.call {
            getAirQuality(stationId)
        }.let { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> apiResult.data.toEntityModel(
                    isCurrentLocationQuality = false
                )
                is ApiResult.Error -> null
            }
        }.let { airQualityEntity ->
            if (airQualityEntity != null) {
                airQualityDao.insert(airQualityEntity)
                SaveStationUseCase.Result.Success
            } else {
                SaveStationUseCase.Result.Error
            }
        }

    private fun AirQualityResponse.toEntityModel(
        isCurrentLocationQuality: Boolean
    ): AirQualityEntity =
        AirQualityEntity(
            stationId = if (isCurrentLocationQuality) Int.MIN_VALUE else stationId,
            primaryAddress = city.name.split(", ").dropLast(1).joinToString(", "),
            secondaryAddress = city.name.split(", ").last(),
            airQualityIndex = airQualityIndex,
            sulfurOxide = individualIndices.sulfurOxide?.value,
            ozone = individualIndices.ozone?.value,
            particle10 = individualIndices.particle10?.value,
            particle25 = individualIndices.particle25?.value,
            isCurrentLocationQuality = isCurrentLocationQuality,
            localTimeRecorded = time.localTimeRecorded,
        )
}