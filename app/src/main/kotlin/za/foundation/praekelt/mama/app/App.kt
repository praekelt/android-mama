package za.foundation.praekelt.mama.app

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager

/**
 * Created by eduardokolomajr on 2015/06/25.
 * Application class
 */

class App:Application(){
    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
    }
}

