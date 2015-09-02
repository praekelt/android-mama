package za.foundation.praekelt.mama.app.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.activity_main.drawer_layout
import kotlinx.android.synthetic.activity_main.nav_view
import kotlinx.android.synthetic.include_main_activity_view_pager.simple_toolbar
import kotlinx.android.synthetic.include_main_activity_view_pager.tabs
import kotlinx.android.synthetic.include_main_activity_view_pager.viewpager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.adapter
import rx.Observable
import rx.observers.Observers
import rx.subscriptions.CompositeSubscription
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerMainActivityComponent
import za.foundation.praekelt.mama.inject.component.MainActivityComponent
import za.foundation.praekelt.mama.inject.module.MainActivityModule
import za.foundation.praekelt.mama.util.otto.ActionPost
import za.foundation.praekelt.mama.util.otto.FunctionPost
import za.foundation.praekelt.mama.util.otto.ObservablePost
import javax.inject.Inject
import kotlin.properties.Delegates
import za.foundation.praekelt.mama.util.Constants as _C

public class MainActivity : AppCompatActivity(), AnkoLogger {
    var tabPosition: Int = 0
    var fragmentPosition: Int = 0
        private set

    companion object {
        val TAG: String = "MainActivity"
    }

    private object argsKeys {
        val tabPositionKey = "tabPosition"
        val fragmentPositionKey = "fragPosition"
    }

    var ucdService: UCDService by Delegates.notNull()
    var mDrawerLayout: DrawerLayout by Delegates.notNull()
    var navigationView: NavigationView by Delegates.notNull()
        @Inject set
    var viewPager: ViewPager by Delegates.notNull()
        @Inject set
    var tabLayout: TabLayout by Delegates.notNull()
        @Inject set
    var networkObs: Observable<Boolean> by Delegates.notNull()
        @Inject set
    var repoObs: Observable<Repo> by Delegates.notNull()
        @Inject set
    val activityComp: MainActivityComponent by lazy { getActivityComponent() }


    val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = this.simple_toolbar
        setSupportActionBar(toolbar)

        val ab = getSupportActionBar()
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        ab.setDisplayHomeAsUpEnabled(true)

        Observable.just(savedInstanceState)
                .filter { it != null }
                .flatMap { Observable.from(it?.keySet()) }
                .subscribe { key ->
                    info("restoring frag $key")
                    when (key) {
                        argsKeys.tabPositionKey -> tabPosition = savedInstanceState!!.getInt(key, 0)
                    }
                }

        mDrawerLayout = this.drawer_layout
        navigationView = this.nav_view
        viewPager = this.viewpager
        tabLayout = this.tabs
        activityComp.inject(this)
        viewPager.setCurrentItem(tabPosition)
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        println("resuming")

        networkObs.filter { !it }
                .subscribe { noInternetSnackBar() }

        val sub = Observers.create<Any>(
                { evt -> tabLayout = activityComp.tabLayout() },
                { err -> info("Error connecting to network ###=> ${err.getMessage()}") })

        subscriptions.add(cloneObs.subscribe(sub))
        subscriptions.add(updateObs.subscribe(sub))
        println("end resuming")
    }

    override fun onPause() {
        super<AppCompatActivity>.onPause()
        if (subscriptions.hasSubscriptions())
            subscriptions.unsubscribe()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(argsKeys.tabPositionKey, tabLayout.getSelectedTabPosition())
        super<AppCompatActivity>.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item!!.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super<AppCompatActivity>.onOptionsItemSelected(item)
    }

    val appComp = fun(): ApplicationComponent {
        return (getApplication() as App).getApplicationComponent()
    }

    fun getActivityComponent(): MainActivityComponent {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(appComp())
                .mainActivityModule(MainActivityModule(this))
                .build()
    }

    fun noInternetSnackBar() {
        Snackbar.make(
                this.drawer_layout, getString(R.string.no_internet_connection),
                Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        //NB: Must set view pager to null otherwise will crash onResume
        //if app is run for first time and is rotated while empty list
        //notification is shown
        viewPager.adapter = null
        activityComp.currentCommitFunc().act = null
        activityComp.saveCommitAction().act = null
        activityComp.bus().post(ObservablePost(TAG, listOf(repoObs)))
        activityComp.bus().post(FunctionPost(TAG, listOf(activityComp.currentCommitFunc())))
        activityComp.bus().post(ActionPost(TAG, listOf(activityComp.saveCommitAction())))
        super<AppCompatActivity>.onDestroy()
    }
}
