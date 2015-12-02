package za.foundation.praekelt.mama.app.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.activity_browse_all.*
import org.jetbrains.anko.AnkoLogger
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.viewmodel.BrowseAllActivityViewModel
import za.foundation.praekelt.mama.databinding.ActivityBrowseAllBinding


/**
 * Created by eduardokolomajr on 15/11/10.
 */
class BrowseAllActivity: AppCompatActivity(), AnkoLogger {
    val viewModel: BrowseAllActivityViewModel = BrowseAllActivityViewModel(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBrowseAllBinding = DataBindingUtil.setContentView(this, R.layout.activity_browse_all)
        setSupportActionBar(this.simple_toolbar)
        setupNavigationDrawerWithUp(this.drawer_layout, this.nav_view, R.id.nav_browse_all)
        title = ""
        viewModel.onAttachActivity(this)
        binding.browseAllActVM = viewModel
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}