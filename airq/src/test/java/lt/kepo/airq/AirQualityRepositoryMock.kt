package lt.kepo.airq

import android.location.Location
import lt.kepo.core.network.ApiResponse
import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.airq.db.model.*
import java.util.*

class AirQualityRepositoryMock :
    lt.kepo.airquality.repository.AirQualityRepository {
    override suspend fun updateAirQualityHere(location: Location?): lt.kepo.core.network.ApiResponse<lt.kepo.core.model.AirQuality> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addAirQuality(stationId: Int): lt.kepo.core.model.AirQuality {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLocalAirQualityHere(): lt.kepo.core.model.AirQuality {
        return airQualityLocal
    }

    override suspend fun getLocalAirQuality(stationId: Int): lt.kepo.core.model.AirQuality {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertLocalAirQuality(airQuality: lt.kepo.core.model.AirQuality) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val airQualityLocal = lt.kepo.core.model.AirQuality(
            1,
            "15",
            "o3",
            lt.kepo.core.model.IndividualIndices(
                lt.kepo.core.model.PropertyDouble(1.0),
                lt.kepo.core.model.PropertyDouble(2.0),
                lt.kepo.core.model.PropertyDouble(3.0),
                lt.kepo.core.model.PropertyDouble(4.0)
            ),
            lt.kepo.core.model.City("Test City"),
            lt.kepo.core.model.Time(Date()),
            true
        )
    }
}