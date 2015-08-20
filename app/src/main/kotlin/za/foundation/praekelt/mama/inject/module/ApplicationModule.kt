package za.foundation.praekelt.mama.inject.module

import android.content.Context
import com.squareup.otto.Bus
import dagger.Module
import dagger.Provides
import za.foundation.praekelt.mama.app.App
import javax.inject.Singleton

/**
 * Dagger Application Module
 */
Module
class ApplicationModule(val app: App){
    Provides
    Singleton
    fun provideApplicationContext(): Context{
        return app
    }

    Provides
    Singleton
    fun provideApplication(): App {
        return app
    }

    Provides
    Singleton
    fun provideBus(): Bus {
        return app.bus
    }
}
