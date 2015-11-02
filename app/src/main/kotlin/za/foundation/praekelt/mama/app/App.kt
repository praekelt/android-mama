package za.foundation.praekelt.mama.app

import android.app.Activity
import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager
import com.squareup.leakcanary.LeakCanary
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import rx.functions.Action
import rx.functions.Function
import za.foundation.praekelt.mama.app.viewmodel.BaseActivityViewModel
import za.foundation.praekelt.mama.app.viewmodel.BaseViewModel
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerApplicationComponent
import za.foundation.praekelt.mama.inject.module.ApplicationModule
import za.foundation.praekelt.mama.util.otto.ViewModelPost
import java.util.HashMap

/**
 * Created by eduardokolomajr on 2015/06/25.
 * Application class
 */

class App : Application(), AnkoLogger {
    val bus: Bus
    val viewModels: MutableMap<String, BaseViewModel>
    val observables: MutableMap<String, List<Observable<out Any>>>
    val functions: MutableMap<String, List<Function>>
    val actions: MutableMap<String, List<Action>>

    init {
        bus = Bus()
        viewModels = HashMap<String, BaseViewModel>()
        observables = HashMap<String, List<Observable<out Any>>>()
        functions = HashMap<String, List<Function>>()
        actions = HashMap<String, List<Action>>()
    }

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
        bus.register(this)
        try {
            LeakCanary.install(this)
        }catch(e: Exception){}
    }

    fun getApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this)).build()
    }

    @Subscribe
    fun saveActivityViewModel(pair: ViewModelPost): Unit{
        viewModels.put(pair.first, pair.second)
    }

    fun getCachedViewModel(id: String): BaseViewModel? {
        if (viewModels.contains(id)) println("found cached view model") else println("no cached view model")
        return viewModels.remove(id)
    }
}

