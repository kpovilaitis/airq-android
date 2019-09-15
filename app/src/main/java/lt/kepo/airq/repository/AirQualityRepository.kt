package lt.kepo.airq.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import lt.kepo.airq.db.model.AirQuality

interface AirQualityRepository {
    val location: MutableLiveData<Location?>

    suspend fun getRemoteHere(): AirQuality

    suspend fun getRemoteByStationId(stationId: Int): AirQuality

    suspend fun getLocalHere(): AirQuality

    suspend fun getLocalByStationId(stationId: Int): AirQuality

    suspend fun insert(airQuality: AirQuality): Long
}