package za.foundation.praekelt.mama.inject.component

import dagger.Component
import za.foundation.praekelt.mama.app.activity.DetailPageActivity
import za.foundation.praekelt.mama.inject.module.DetailPageActivityModule
import za.foundation.praekelt.mama.inject.scope.ActivityScope

/**
 * Created by eduardokolomajr on 2015/09/14.
 */
@ActivityScope
@Component(modules = arrayOf(DetailPageActivityModule::class))
public interface DetailPageActivityComponent{
    fun inject(activity: DetailPageActivity): Unit
}