package lt.kepo.airquality.domain

import lt.kepo.core.model.AirQuality
import java.util.*

fun AirQuality.shouldUpdate() = updatedAt?.before(Date(Date().time - 1000 * 60 * 10)) == true