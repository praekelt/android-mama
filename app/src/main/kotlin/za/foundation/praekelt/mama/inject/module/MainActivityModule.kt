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
import rx.functions.Action
import rx.functions.Action1
import rx.functions.Func1
import rx.functions.Function
import rx.schedulers.Schedulers
import za.foundation.praekelt.mama.api.db.util.DBTransaction
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.createUCDService
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.api.rest.model.RepoStatus
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.scope.ActivityScope
import za.foundation.praekelt.mama.util.SharedPrefsUtil
import java.util.concurrent.TimeUnit
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * Dagger module for main activity
 */
Module
class MainActivityModule(val activity: MainActivity) : AnkoLogger {
    val locale: String

    init {
        println("init module")
        locale: String = activity.defaultSharedPreferences
                .getString(_C.SHARED_PREFS_LOCALE, _C.SHARED_PREFS_LOCALE_DEFAULT)
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
        return CategoryPageAdapter(activity.getSupportFragmentManager(), locale)
    }

    Provides
    ActivityScope
    fun provideViewPager(categoryPagerAdapter: CategoryPageAdapter): ViewPager {
        activity.viewPager.setAdapter(categoryPagerAdapter)
        return activity.viewPager
    }

    Provides
    ActivityScope
    fun provideTabLayout(viewPager: ViewPager): TabLayout {
        info("getting tl")
        viewPager.setCurrentItem(activity.tabPosition)
        activity.tabLayout.setupWithViewPager(viewPager)
        return activity.tabLayout
    }

    Provides
    fun providesNetworkObservable(): Observable<Boolean> {
        return Observable.just(activity.connectivityManager
                .getActiveNetworkInfo()?.isConnected() ?: false)
    }

    Provides
    ActivityScope
    fun providesCurrentCommitFunction(): CurrentCommitFunc {
        val cached: List<Function>? = activity.appComp().app().getCachedFunction(MainActivity.TAG)
        if (cached != null) {
            println("changing cached current function  activity")
            (cached[0] as CurrentCommitFunc).act = activity
            return (cached[0] as CurrentCommitFunc)
        } else
            return CurrentCommitFunc(activity)
    }

    Provides
    ActivityScope
    fun providesCompareCommitFunction(): CompareCommitFunc {
        val cached: List<Function>? = activity.appComp().app().getCachedFunction(MainActivity.TAG)
        if (cached != null) {
            println("changing cached compare function activity")
            (cached[1] as CompareCommitFunc).act = activity
            return (cached[0] as CompareCommitFunc)
        } else
            return CompareCommitFunc(activity)
    }

    Provides
    ActivityScope
    fun provideSaveCommitAction(): SaveCommitAction {
        val cached: List<Action>? = activity.appComp().app().getCachedAction(MainActivity.TAG)
        if (cached != null) {
            (cached[0] as SaveCommitAction).act = activity
            return (cached[0] as SaveCommitAction)
        } else
            return SaveCommitAction(activity)
    }

    Provides
    ActivityScope
    fun provideRepoObservable(networkObs: Observable<Boolean>, saveCommitAction: SaveCommitAction,
                              currentCommitFunc: CurrentCommitFunc, ucdService: UCDService,
                              compareCommitFunc: CompareCommitFunc): Observable<Repo>{
        info("merging both obs")

        val obs: Observable<List<Observable<out Any>>?> =
                Observable.just(activity.appComp().app().getCachedObservables(MainActivity.TAG))

        val cachedObs: Observable<Repo> = obs.filter { it != null }
                .doOnNext{ info("merging cached obs")}
            .flatMap{ (it!![0] as Observable<Repo>) }

        val freshObs: Observable<Repo> = obs.filter { it == null }
                .doOnNext{ info("merging fresh obs") }
                .flatMap{ createCloneObs(networkObs, currentCommitFunc, ucdService)
                            .mergeWith(
                                    createUpdateObs(networkObs, currentCommitFunc,
                                            compareCommitFunc, ucdService)
                            )}

        return Observable.merge(cachedObs, freshObs)
                .doOnNext { DBTransaction.saveRepo(it) }
                .doOnNext (saveCommitAction)
                .doOnNext { info("SP saved") }
                .doOnNext { Observable.interval(500, TimeUnit.MILLISECONDS).toBlocking().first() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
    }

    Provides
    ActivityScope
    fun provideUCDService(): UCDService {
        return createUCDService()
    }

    fun createCloneObs(networkObs: Observable<Boolean>, currentCommitFunc: CurrentCommitFunc,
                       ucdService: UCDService):
            Observable<Repo>{
        return networkObs.filter { it }
                .map(currentCommitFunc)
                .map { it != "" }
                .filter { !it }
                .doOnNext { info("repo doesn't exist") }
                .flatMap { ucdService.cloneRepo() }
    }

    fun createUpdateObs(networkObs: Observable<Boolean>, currentCommitFunc: CurrentCommitFunc,
                        compareCommitFunc: CompareCommitFunc, ucdService: UCDService):
            Observable<RepoPull>{
        return networkObs.filter { it }
                .map(currentCommitFunc)
                .map { it != "" }
                .filter { it }
                .doOnNext { info("repo exists, checking for update") }
                .flatMap { ucdService.getRepoStatus() }
                .filter(compareCommitFunc)
                .map { it.commit != "" }
                .map(currentCommitFunc)
                .doOnNext { info("getting update") }
                .flatMap {
                    ucdService.pullRepo(it)
                }
    }

    abstract class CommitFunc<I, R>(var act: Context?) : Func1<I, R>

    class CurrentCommitFunc(act: Context?) : CommitFunc<Boolean, String>(act) {
        override fun call(t: Boolean): String {
            return SharedPrefsUtil.getCommitFromSharedPrefs(act!!)
        }
    }

    class CompareCommitFunc(act: Context?) : CommitFunc<RepoStatus, Boolean>(act) {
        override fun call(repo: RepoStatus?): Boolean? {
            return SharedPrefsUtil.getCommitFromSharedPrefs(act!!) != repo!!.commit
        }
    }

    class SaveCommitAction(var act: Context?) : Action1<Repo> {
        override fun call(repo: Repo): Unit {
            SharedPrefsUtil.saveCommitToSharedPrefs(act!!, repo.commit)
        }
    }
}