package lt.kepo.core.ui

interface AppNavigator{
    fun startAirQuality()
    fun startStations()
    fun finishActivity(resultCode: Int? = null)
}
