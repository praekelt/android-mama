package za.foundation.praekelt.mama.app.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.activity_browse_all.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onItemSelectedListener
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.viewmodel.BrowseAllActivityViewModel
import za.foundation.praekelt.mama.databinding.ActivityBrowseAllBinding
import za.foundation.praekelt.mama.inject.component.ApplicationComponent
import za.foundation.praekelt.mama.inject.component.BrowseAllActivityComponent
import za.foundation.praekelt.mama.inject.component.DaggerBrowseAllActivityComponent
import za.foundation.praekelt.mama.inject.module.BrowseAllActivityModule
import javax.inject.Inject


/**
 * Created by eduardokolomajr on 15/11/10.
 */
class BrowseAllActivity: AppCompatActivity(), AnkoLogger {
    @Inject lateinit var viewModel: BrowseAllActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBrowseAllBinding = DataBindingUtil.setContentView(this, R.layout.activity_browse_all)
        setSupportActionBar(this.simple_toolbar)
        setupNavigationDrawerWithUp(this.drawer_layout, this.nav_view, R.id.nav_browse_all)
        title = ""
        activityComponent().inject(this)
        viewModel.onAttachActivity(this)
        binding.browseAllActVM = viewModel
        toolbar_category_spinner.onItemSelectedListener {
            onItemSelected { parent, view, position, rowId -> viewModel.selectedCategory = position }
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

    fun appComponent(): ApplicationComponent = (application as App).getApplicationComponent()

    fun activityComponent(): BrowseAllActivityComponent {
        return DaggerBrowseAllActivityComponent.builder()
                .applicationComponent(appComponent())
                .browseAllActivityModule(BrowseAllActivityModule(this))
                .build()
    }
}