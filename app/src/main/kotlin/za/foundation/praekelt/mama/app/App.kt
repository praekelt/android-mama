package za.foundation.praekelt.mama.app

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerApplicationComponent
import za.foundation.praekelt.mama.inject.module.ApplicationModule

/**
 * Created by eduardokolomajr on 2015/06/25.
 * Application class
 */

class App:Application(){
    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
    }
}

