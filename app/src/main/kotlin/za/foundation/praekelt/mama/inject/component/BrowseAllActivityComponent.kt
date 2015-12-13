package za.foundation.praekelt.mama.inject.component

import com.squareup.otto.Bus
import dagger.Component
import za.foundation.praekelt.mama.app.activity.BrowseAllActivity
import za.foundation.praekelt.mama.inject.module.BrowseAllActivityModule
import za.foundation.praekelt.mama.inject.scope.ActivityScope

/**
 * Dagger component for BrowseAllActivityViewModel
 */
@ActivityScope
@Component(modules = arrayOf(BrowseAllActivityModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface BrowseAllActivityComponent {
    fun inject(activity: BrowseAllActivity): Unit

    fun bus(): Bus
}