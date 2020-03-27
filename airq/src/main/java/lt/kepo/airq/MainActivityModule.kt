package lt.kepo.airq

import android.content.Context
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import lt.kepo.core.navigation.NavigationContainer

@Module
@InstallIn(ActivityComponent::class)
class MainActivityModule {

    @Provides
    fun provideNavigationContainer(
        @ActivityContext context: Context
    ): NavigationContainer =
        context as MainActivity

    @Provides
    fun provideFragmentManager(
        @ActivityContext context: Context
    ): FragmentManager =
        (context as MainActivity).supportFragmentManager
}