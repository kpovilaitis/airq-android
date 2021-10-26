package lt.kepo.airqualitydata

import lt.kepo.airqualitynetwork.response.AirQualityResponse
import lt.kepo.airqualitydatabase.AirQualityEntity

internal fun AirQualityEntity.toListItemModel(): AirQualityListItem =
    AirQualityListItem(
        stationId = stationId,
        address = address,
        index = airQualityIndex,
        isCurrentLocation = isCurrentLocationQuality,
    )

internal fun AirQualityEntity.toDetailsModel(): AirQualityDetails =
    AirQualityDetails(
        stationId = stationId,
        address = address,
        airQualityIndex = airQualityIndex,
        sulfurOxide = sulfurOxide,
        ozone = ozone,
        particle10 = particle10,
        particle25 = particle25,
        isCurrentLocationQuality = isCurrentLocationQuality,
        localTimeRecorded = localTimeRecorded,
    )

internal fun AirQualityResponse.toEntityModel(
    isCurrentLocationQuality: Boolean
): AirQualityEntity =
    AirQualityEntity(
        stationId = stationId,
        address = city.name,
        airQualityIndex = airQualityIndex,
        sulfurOxide = individualIndices.sulfurOxide?.value,
        ozone = individualIndices.ozone?.value,
        particle10 = individualIndices.particle10?.value,
        particle25 = individualIndices.particle25?.value,
        isCurrentLocationQuality = isCurrentLocationQuality,
        localTimeRecorded = time.localTimeRecorded,
    )
