package za.foundation.praekelt.mama.app.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.activity_main.drawer_layout
import kotlinx.android.synthetic.activity_main.nav_view
import kotlinx.android.synthetic.include_main_activity_view_pager.simple_toolbar
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.viewmodel.MainActivityViewModel
import za.foundation.praekelt.mama.databinding.ActivityMainBinding
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerMainActivityComponent
import za.foundation.praekelt.mama.inject.component.MainActivityComponent
import za.foundation.praekelt.mama.inject.module.MainActivityModule
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

    var networkObs: Observable<Boolean> by Delegates.notNull()
        @Inject set
    var activityComp: MainActivityComponent by Delegates.notNull()
    var viewModel: MainActivityViewModel by Delegates.notNull()
        @Inject set


    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)

        val toolbar: Toolbar = this.simple_toolbar
        setSupportActionBar(toolbar)

        val ab = getSupportActionBar()
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        ab.setDisplayHomeAsUpEnabled(true)

        this.nav_view.setNavigationItemSelectedListener {
            menuItem ->
            menuItem.setChecked(true)
            this.drawer_layout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        Observable.just(savedInstanceState)
                .filter { it != null }
                .flatMap { Observable.from(it?.keySet()) }
                .subscribe { key ->
                    info("restoring frag $key")
                    when (key) {
                        argsKeys.tabPositionKey -> tabPosition = savedInstanceState!!.getInt(key, 0)
                    }
                }

        activityComp = getActivityComponent()
        activityComp.inject(this)
        viewModel.onAttachActivity(this)
        binding.setMainActVM(viewModel)
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        println("resuming")

        networkObs.filter { !it }
                .subscribe { noInternetSnackBar() }
        println("end resuming")
    }

    override fun onPause() {
        super<AppCompatActivity>.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        //outState?.putInt(argsKeys.tabPositionKey, tabLayout.getSelectedTabPosition())
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

    fun appComp(): ApplicationComponent {
        return (getApplication() as App).getApplicationComponent()
    }

    fun getActivityComponent(): MainActivityComponent {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(appComp())
                .mainActivityModule(MainActivityModule(this))
                .build()
    }

    fun noInternetSnackBar(): Unit {
        Snackbar.make(
                this.drawer_layout, getString(R.string.no_internet_connection),
                Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        info("start destroy")
        super<AppCompatActivity>.onDestroy()
        viewModel.onDestroy()
        info("end destroy")
    }
}
