package za.foundation.praekelt.mama.app.viewmodel

import android.content.Context
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.connectivityManager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import za.foundation.praekelt.mama.BR
import za.foundation.praekelt.mama.api.db.util.DBTransaction
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Category_Table
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.createUCDService
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.util.SharedPrefsUtil
import za.foundation.praekelt.mama.util.otto.ActivityViewModelPost
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * ViewModel for MainActivity
 * Current a problem with Dagger and this class, doesn't build factory classes
 */
public class MainActivityViewModel(mainActivity: MainActivity) :
        BaseActivityViewModel<MainActivity>(mainActivity), AnkoLogger {
    var repoObs: Observable<Repo> by Delegates.notNull()
    var fm: FragmentManager?
    var app: App
    @Bindable var vp: ViewPager? = null
    @Bindable var categories: ObservableArrayList<Category> = ObservableArrayList()

    companion object{
        val TAG: String = "MainActivityViewModel"
    }

    init {
        println("init MAVM")
        fm = mainActivity.getSupportFragmentManager()
        app = mainActivity.appComp().app()
        repoObs = createRepoObservable(getNetworkObservable(), createUCDService())

        repoObs.doOnNext{ refreshCategories() }
                .doOnNext { Observable.interval(500, TimeUnit.MILLISECONDS).toBlocking().first() }
                .subscribe(
                { repo -> info("repo received")
                    notifyPropertyChanged(BR.vp) },
                { err -> info("error getting/updating repo") }
        )
    }

    override fun onCreate() {
        super<BaseActivityViewModel>.onCreate()
    }

    override fun onAttachActivity(activity: MainActivity) {
        info("start attach activity")
        super<BaseActivityViewModel>.onAttachActivity(activity)
        refreshCategories()
        vp = act?.get()?.viewPager
        fm = act?.get()?.getSupportFragmentManager()
        notifyPropertyChanged(BR.vp)
        info("end attach activity")
    }

    override fun onDestroy() {
        fm = null
        vp = null
        act?.get()?.appComp()?.bus()?.post(ActivityViewModelPost(TAG, this))
        super<BaseActivityViewModel>.onDestroy()
    }

    fun getNetworkObservable(): Observable<Boolean> {
        return Observable.just(app.connectivityManager
                                       .getActiveNetworkInfo()?.isConnected() ?: false)
    }

    /**
     * Creates an observable used to clone the repo for the first time
     */
    fun createCloneObs(networkObs: Observable<Boolean>, ucdService: UCDService, ctx: Context):
            Observable<Repo> {
        return networkObs.filter { it }
                .map { SharedPrefsUtil.getCommitFromSharedPrefs(ctx) }
                .filter { it == "" }
                .doOnNext { info("repo doesn't exist") }
                .flatMap { ucdService.cloneRepo() }
    }

    /**
     * Creates an observable used to pull changes from the repo
     */
    fun createUpdateObs(networkObs: Observable<Boolean>, ucdService: UCDService, ctx: Context):
            Observable<RepoPull> {
        return networkObs.filter { it }
                .map { SharedPrefsUtil.getCommitFromSharedPrefs(ctx) }
                .map { it != "" }
                .filter { it }
                .doOnNext { info("repo exists, checking for update") }
                .flatMap { ucdService.getRepoStatus() }
                .filter { SharedPrefsUtil.getCommitFromSharedPrefs(ctx) != it.commit }
                .map { it.commit != "" }
                .map { SharedPrefsUtil.getCommitFromSharedPrefs(ctx) }
                .doOnNext { info("getting update") }
                .flatMap { ucdService.pullRepo(it) }
    }

    fun createRepoObservable(networkObs: Observable<Boolean>,
                             ucdService: UCDService): Observable<Repo> {
        //If no cached observable list found generate a new one by merging a clone observable with
        //an update observable
        return createCloneObs(networkObs, ucdService, app)
                .mergeWith(createUpdateObs(networkObs, ucdService, app))
                .doOnNext { DBTransaction.saveRepo(it) }
                .doOnNext { SharedPrefsUtil.saveCommitToSharedPrefs(app, it.commit) }
                .doOnNext { info("SP saved") }
                .doOnNext { Observable.interval(500, TimeUnit.MILLISECONDS).toBlocking().first() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun refreshCategories(): Unit {
        categories.clear()
        categories.addAll(Select().from(javaClass<Category>())
                .where(Condition.column(Category_Table.FEATUREDINNAVBAR).`is`(true))
                .and(Condition.column(Category_Table.LOCALEID).`is`(
                        _C.SHARED_PREFS_LOCALE_DEFAULT)).queryList())
    }
}