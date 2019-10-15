package lt.kepo.airq.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.db.model.Station
import lt.kepo.airq.data.repository.stations.StationsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject


class UpdateLocalStationsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    private val repository by inject<StationsRepository>()

    override suspend fun doWork(): Result = coroutineScope {
        val stations = repository.getLocalAllStations()

        val jobs = stations.map {
            async {
                when (val response = repository.getRemoteStation(it.id)) {
                    is ApiSuccessResponse -> {
                        val responseStation = response.data

                        stations.find{ it.id == responseStation.stationId }?.airQualityIndex = responseStation.airQualityIndex

                        withContext(Dispatchers.IO) { repository.insertLocalStation( it ) }
                    }
                    else -> Result.failure()
                }
            }
        }

        jobs.awaitAll()

        Result.success()
    }
}