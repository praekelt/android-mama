package za.foundation.praekelt.mama.inject.module

import android.app.Activity
import android.content.Context
import android.support.design.widget.NavigationView
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.connectivityManager
import rx.Observable
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.app.viewmodel.BaseActivityViewModel
import za.foundation.praekelt.mama.app.viewmodel.MainActivityViewModel
import za.foundation.praekelt.mama.inject.scope.ActivityScope
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * Dagger module for main activity
 */
Module
class MainActivityModule(val activity: MainActivity) : AnkoLogger {
    init {
        println("init module")
    }

    Provides
    ActivityScope
    fun provideActivityContext(): Context {
        return activity
    }

    Provides
    ActivityScope
    fun provideNav(): NavigationView {
        activity.navigationView.setNavigationItemSelectedListener {
            menuItem ->
            menuItem.setChecked(true)
            activity.mDrawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
        return activity.navigationView
    }

    Provides
    ActivityScope
    fun ProvideViewModel(): MainActivityViewModel {
        return MainActivityViewModel(activity)
    }

    //Observable to check whether device is connected to a network or not
    Provides
    fun providesNetworkObservable(): Observable<Boolean> {
        return Observable.just(activity.connectivityManager
                                       .getActiveNetworkInfo()?.isConnected() ?: false)
    }


}