package za.foundation.praekelt.mama.inject.component

import dagger.Component
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.module.MainActivityModule
import za.foundation.praekelt.mama.inject.module.RestModule
import za.foundation.praekelt.mama.inject.scope.ActivityScope

ActivityScope
Component(modules = arrayOf(MainActivityModule::class, RestModule::class))
public interface MainActivityComponent{
    fun inject(activity: MainActivity): Unit
}
