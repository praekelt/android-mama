package za.foundation.praekelt.mama.inject.module

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.defaultSharedPreferences
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import za.foundation.praekelt.mama.api.db.util.DBTransaction
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.createUCDService
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.scope.ActivityScope
import za.foundation.praekelt.mama.util.SharedPrefsUtil
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * Dagger module for main activity
 */
Module
class MainActivityModule(val activity: MainActivity) : AnkoLogger {
    val categoryPageAdapter: CategoryPageAdapter

    init {
        println("init module")
        val locale: String = activity.defaultSharedPreferences
                .getString(_C.SHARED_PREFS_LOCALE, _C.SHARED_PREFS_LOCALE_DEFAULT)
        categoryPageAdapter = CategoryPageAdapter(activity.getSupportFragmentManager(), locale)
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
    fun provideFragmentManager(): FragmentManager {
        return activity.getSupportFragmentManager()
    }

    Provides
    ActivityScope
    fun provideCategoryPageAdapter(): CategoryPageAdapter {
        categoryPageAdapter.refresh().subscribe { categoryPageAdapter.notifyDataSetChanged() }
        return categoryPageAdapter
    }

    Provides
    ActivityScope
    fun provideViewPager(categoryPagerAdapter: CategoryPageAdapter): ViewPager {
        println("addr => $this")
        activity.viewPager.setAdapter(categoryPagerAdapter)
        return activity.viewPager
    }

    Provides
    ActivityScope
    fun provideTabLayout(viewPager: ViewPager): TabLayout {
        activity.tabLayout.setupWithViewPager(viewPager)
        return activity.tabLayout
    }

    Provides
    ActivityScope
    fun providesNetworkObservable(): Observable<Boolean> {
        return Observable.just(activity.connectivityManager.getActiveNetworkInfo().isConnected())
    }

    Provides
    ActivityScope
    fun provideCloneRepoObservable(networkObs: Observable<Boolean>, ucdService: UCDService): Observable<Repo> {
        val cached: List<Observable<out Any>>? = activity.appComp().app().getCachedObservables(activity.TAG)
        return if (cached != null)
            cached[0] as Observable<Repo>
        else
            networkObs.filter { it }
                    .map { SharedPrefsUtil.getCommitFromSharedPrefs(activity) }
                    .map { it != "" }
                    .filter { !it }
                    .doOnNext { info("repo doesn't exist") }
                    .doOnNext { println("repo doesn't exist") }
                    .flatMap { ucdService.cloneRepo() }
                    .doOnNext { DBTransaction.saveRepo(it) }
                    .doOnNext { SharedPrefsUtil.saveCommitToSharedPrefs(activity, it.commit) }
                    .doOnNext { println("SP saved") }
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
    }

    Provides
    ActivityScope
    fun provideUpdateRepoObservable(networkObs: Observable<Boolean>, ucdService: UCDService): Observable<RepoPull> {
        val cached: List<Observable<out Any>>? = activity.appComp().app().getCachedObservables("MainActivity")
        return if (cached != null)
            cached[1] as Observable<RepoPull>
        else
            networkObs.filter { it }
                    .map { SharedPrefsUtil.getCommitFromSharedPrefs(activity) }
                    .map { it != "" }
                    .filter { it }
                    .doOnNext { info("repo exists, checking for update") }
                    .flatMap { ucdService.getRepoStatus() }
                    .filter { SharedPrefsUtil.getCommitFromSharedPrefs(activity) != it.commit }
                    .doOnNext { info("getting update") }
                    .flatMap {
                        ucdService.pullRepo(SharedPrefsUtil.getCommitFromSharedPrefs(activity))
                    }
                    .doOnNext { DBTransaction.saveRepoPull(it) }
                    .doOnNext { println("saved update data") }
                    .doOnNext { SharedPrefsUtil.saveCommitToSharedPrefs(activity, it.commit) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
    }

    Provides
    ActivityScope
    fun provideUCDService(): UCDService {
        return createUCDService()
    }
}