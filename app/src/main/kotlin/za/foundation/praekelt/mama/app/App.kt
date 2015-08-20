package za.foundation.praekelt.mama.app

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerApplicationComponent
import za.foundation.praekelt.mama.inject.module.ApplicationModule
import java.util.HashMap

/**
 * Created by eduardokolomajr on 2015/06/25.
 * Application class
 */

class App : Application(), AnkoLogger {
    val bus: Bus
    val observables: MutableMap<String, List<Observable<out Any>>>

    init {
        bus = Bus()
        observables = HashMap<String, List<Observable<out Any>>>()
    }

    override fun onCreate() {
        super<Application>.onCreate()
        FlowManager.init(this)
        bus.register(this)
    }

    override fun onLowMemory() {
        super<Application>.onLowMemory()
        observables.clear()
    }

    override fun onTerminate() {
        super<Application>.onTerminate()
        observables.clear()
        bus.unregister(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
    }

    @Subscribe
    fun saveObservables(pair: Pair<String, List<Observable<Any>>>): Unit {
        info("pair received")
        observables.put(pair.first, pair.second)
    }

    fun getCachedObservables(id: String): List<Observable<out Any>>? {
        if (observables.contains(id)) println("found cached item") else println("no cached item")
        return if (observables.contains(id)) observables[id] else null
    }
}

