package lt.kepo.airq

import android.location.Location
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.db.model.City
import lt.kepo.airq.db.model.IndividualIndices
import lt.kepo.airq.db.model.Property
import lt.kepo.airq.data.repository.airquality.AirQualityRepository

class AirQualityRepositoryMock : AirQualityRepository{
    override suspend fun getRemoteAirQualityHere(): ApiResponse<AirQualityDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getRemoteAirQualityHere(location: Location): ApiResponse<AirQualityDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getRemoteAirQuality(stationId: Int): AirQuality {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLocalAirQualityHere(): AirQuality {
        return airQualityLocal
    }

    override suspend fun getLocalAirQuality(stationId: Int): AirQuality {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun upsertLocalAirQualityHere(airQuality: AirQuality) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val airQualityLocal = AirQuality(
            1,
            "15",
            "o3",
            IndividualIndices(
                Property(1.0),
                Property(2.0),
                Property(3.0),
                Property(4.0)
            ),
            City("Test City"),
            true
        )
    }
}