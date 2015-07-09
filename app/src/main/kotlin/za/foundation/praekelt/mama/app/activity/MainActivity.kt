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
import com.raizlabs.android.dbflow.runtime.TransactionManager
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction
import kotlinx.android.synthetic.activity_main.drawer_layout
import kotlinx.android.synthetic.activity_main.nav_view
import kotlinx.android.synthetic.include_main_activity_view_pager.simple_toolbar
import kotlinx.android.synthetic.include_main_activity_view_pager.tabs
import kotlinx.android.synthetic.include_main_activity_view_pager.viewpager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.defaultSharedPreferences
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.inject.component.DaggerMainActivityComponent
import za.foundation.praekelt.mama.inject.component.MainActivityComponent
import za.foundation.praekelt.mama.inject.module.MainActivityModule
import za.foundation.praekelt.mama.inject.module.RestModule
import javax.inject.Inject
import kotlin.properties.Delegates
import za.foundation.praekelt.mama.util.Constants as _C

public class MainActivity : AppCompatActivity(), AnkoLogger {

    var ucdService: UCDService by Delegates.notNull()
        @Inject set
    var mDrawerLayout: DrawerLayout by Delegates.notNull()
    var navigationView: NavigationView by Delegates.notNull()
        @Inject set
    var viewPager: ViewPager by Delegates.notNull()
        @Inject set
    var tabLayout: TabLayout by Delegates.notNull()
        @Inject set
    private val actComponent by lazy { getActivityComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = this.simple_toolbar
        setSupportActionBar(toolbar)

        val ab = getSupportActionBar()
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        ab.setDisplayHomeAsUpEnabled(true)

        mDrawerLayout = this.drawer_layout
        navigationView = this.nav_view
        viewPager = this.viewpager
        tabLayout = this.tabs
        actComponent.inject(this)
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        val networkObs: Observable<Boolean> = Observable
                .just(connectivityManager.getActiveNetworkInfo())
                .map { networkInfo -> networkInfo?.isConnected() ?: false }

        networkObs.filter { !it }
                .subscribe {
                    Snackbar.make(
                            this.drawer_layout, getString(R.string.no_internet_connection),
                            Snackbar.LENGTH_LONG).show()
                }

        val hasRepoObs: Observable<Boolean> = networkObs.filter { it }
                .map { defaultSharedPreferences.getString(_C.SHARED_PREFS_COMMIT, "") }
                .map { it != "" }

        hasRepoObs.filter { !it }
                .doOnNext { info("repo doesn't exist") }
                .flatMap { ucdService.cloneRepo() }
                .doOnNext { saveLocales(it) }
                .doOnNext { saveCategories(it) }
                .doOnNext { savePages(it) }
                .doOnNext {
                    defaultSharedPreferences.edit()
                            .putString(_C.SHARED_PREFS_COMMIT, it.commit).apply() }
                .flatMap {
                    info("refreshing")
                    return@flatMap (viewPager.getAdapter() as CategoryPageAdapter).refresh() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getActivityComponent().inject(this)
                }
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

    fun getActivityComponent(): MainActivityComponent {
        return DaggerMainActivityComponent.builder()
                .mainActivityModule(MainActivityModule(this))
                .restModule(RestModule())
                .build()
    }

    fun saveLocales(repo: Repo): Boolean {
        info("saving locales info")
        TransactionManager.getInstance()
                .addTransaction(SaveModelTransaction(ProcessModelInfo.withModels(repo.locales)))
        return true
    }

    fun saveCategories(repo: Repo): Boolean {
        info("saving categories info")
        TransactionManager.getInstance()
                .addTransaction(SaveModelTransaction(ProcessModelInfo.withModels(repo.categories)))
        return true
    }

    fun savePages(repo: Repo): Boolean {
        info("saving pages info")
        TransactionManager.getInstance()
                .addTransaction(SaveModelTransaction(ProcessModelInfo.withModels(repo.pages)))
        return true
    }
}
