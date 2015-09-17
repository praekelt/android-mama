package za.foundation.praekelt.mama.inject.component

import android.support.design.widget.TabLayout
import com.squareup.otto.Bus
import dagger.Component
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.module.MainActivityModule
import za.foundation.praekelt.mama.inject.scope.ActivityScope

ActivityScope
Component(modules = arrayOf(MainActivityModule::class), dependencies = arrayOf(ApplicationComponent::class))
public interface MainActivityComponent {
    fun inject(activity: MainActivity): Unit

    fun bus(): Bus
}
