package za.foundation.praekelt.mama.app

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager
import com.squareup.leakcanary.LeakCanary
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import rx.functions.Action
import rx.functions.Function
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerApplicationComponent
import za.foundation.praekelt.mama.inject.module.ApplicationModule
import za.foundation.praekelt.mama.util.otto.ActionPost
import za.foundation.praekelt.mama.util.otto.FunctionPost
import za.foundation.praekelt.mama.util.otto.ObservablePost
import java.util.HashMap

/**
 * Created by eduardokolomajr on 2015/06/25.
 * Application class
 */

class App : Application(), AnkoLogger {
    val bus: Bus
    val observables: MutableMap<String, List<Observable<out Any>>>
    val functions: MutableMap<String, List<Function>>
    val actions: MutableMap<String, List<Action>>

    init {
        bus = Bus()
        observables = HashMap<String, List<Observable<out Any>>>()
        functions = HashMap<String, List<Function>>()
        actions = HashMap<String, List<Action>>()
    }

    override fun onCreate() {
        super<Application>.onCreate()
        FlowManager.init(this)
        bus.register(this)
        LeakCanary.install(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this)).build()
    }

    @Subscribe
    fun saveObservables(pair: ObservablePost): Unit {
        observables.put(pair.first, pair.second)
    }

    @Subscribe
    fun saveFunctions(pair: FunctionPost): Unit {
        functions.put(pair.first, pair.second)
    }

    @Subscribe
    fun saveActions(pair: ActionPost): Unit {
        actions.put(pair.first, pair.second)
    }

    fun getCachedObservables(id: String): List<Observable<out Any>>? {
        if (observables.contains(id)) println("found cached item") else println("no cached item")
        return observables.remove(id)
    }

    fun getCachedFunction(id: String): List<Function>? {
        if (functions.contains(id)) println("found cached function") else println("no cached function")
        return functions.remove(id)
    }

    fun getCachedAction(id: String): List<Action>? {
        if (actions.contains(id)) println("found cached action") else println("no cached action")
        return actions.remove(id)
    }
}

