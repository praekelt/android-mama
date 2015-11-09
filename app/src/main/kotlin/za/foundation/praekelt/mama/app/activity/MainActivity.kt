package za.foundation.praekelt.mama.app.activity

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.activity_main.drawer_layout
import kotlinx.android.synthetic.activity_main.nav_view
import kotlinx.android.synthetic.include_main_activity_view_pager.simple_toolbar
import org.jetbrains.anko.*
import rx.Observable
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.viewmodel.MainActivityViewModel
import za.foundation.praekelt.mama.databinding.ActivityMainBinding
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.DaggerMainActivityComponent
import za.foundation.praekelt.mama.inject.component.MainActivityComponent
import za.foundation.praekelt.mama.inject.module.MainActivityModule
import za.foundation.praekelt.mama.util.otto.PageItemClickedPost
import javax.inject.Inject
import kotlin.properties.Delegates
import za.foundation.praekelt.mama.util.Constants as _C

public class MainActivity : AppCompatActivity(), AnkoLogger {
    var tabPosition: Int = 0
    var fragmentPosition: Int = 0
        private set

    companion object {
        const val TAG: String = "MainActivity"
    }

    private object argsKeys {
        val tabPositionKey = "tabPosition"
        val fragmentPositionKey = "fragPosition"
    }


    val activityComp: MainActivityComponent by lazy { getActivityComponent() }
    lateinit var viewModel: MainActivityViewModel
        @Inject set
    lateinit var networkObs: Observable<Boolean>
        @Inject set

    lateinit var actionbarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)

        savedInstanceState?.keySet()?.forEach {
            when (it) {
                argsKeys.tabPositionKey -> tabPosition = savedInstanceState.getInt(it, 0)
                else -> {}
            }
        }

        setSupportActionBar(this.simple_toolbar)
        actionbarDrawerToggle = setupNavigationDrawer(this.drawer_layout, this.nav_view,
                R.id.nav_featured_stories, this.simple_toolbar)

        activityComp.inject(this)
        viewModel.onAttachActivity(this)
        binding.setMainActVM(viewModel)
    }

    override fun onResume() {
        super.onResume()
//        info("resuming")

        networkObs.filter { !it }
                .subscribe { noInternetSnackBar() }

        activityComp.bus().register(this)
//        info("end resuming")
    }

    override fun onPause() {
        activityComp.bus().unregister(this)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        //outState?.putInt(argsKeys.tabPositionKey, tabLayout.getSelectedTabPosition())
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item!!.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        actionbarDrawerToggle.onConfigurationChanged(newConfig)
    }

    private fun appComp(): ApplicationComponent {
        return (application as App).getApplicationComponent()
    }

    private fun getActivityComponent(): MainActivityComponent {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(appComp())
                .mainActivityModule(MainActivityModule(this))
                .build()
    }

    private fun noInternetSnackBar(): Unit {
        Snackbar.make(
                this.drawer_layout, getString(R.string.no_internet_connection),
                Snackbar.LENGTH_LONG).show()
    }

    @Subscribe
    fun pageClickedEvent(post: PageItemClickedPost){
        toast("item clicked => ${post.pageUuid}")
        startActivity(intentFor<DetailPageActivity>(
                DetailPageActivity.argsKeys.uuidKey to post.pageUuid).singleTop())
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}
