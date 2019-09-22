package lt.kepo.airq.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import lt.kepo.airq.db.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteHere(): AirQuality

    suspend fun getRemoteHereByLocation(location: Location): AirQuality

    suspend fun getRemoteByStationId(stationId: Int): AirQuality

    suspend fun getLocalHere(): AirQuality

    suspend fun getLocalByStationId(stationId: Int): AirQuality
}