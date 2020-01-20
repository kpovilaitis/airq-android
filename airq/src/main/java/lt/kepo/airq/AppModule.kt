package lt.kepo.airq

import lt.kepo.airquality.ui.AirQualitiesNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule : Module = module {
    factory<AirQualitiesNavigator> { AirQualitiesNavigatorImpl() }
}