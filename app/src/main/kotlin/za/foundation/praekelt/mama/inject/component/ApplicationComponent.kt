package za.foundation.praekelt.mama.inject.component

import com.squareup.otto.Bus
import dagger.Component
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.inject.module.ApplicationModule
import javax.inject.Singleton

/**
 * Dagger component for Application
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
public interface ApplicationComponent{
    fun inject (app: App): Unit

    fun bus(): Bus

    fun app(): App
}
