package za.foundation.praekelt.mama.app.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import kotlinx.android.synthetic.activity_browse_all.simple_toolbar
import kotlinx.android.synthetic.activity_browse_all.drawer_layout
import kotlinx.android.synthetic.activity_browse_all.nav_view
import org.jetbrains.anko.support.v4.drawerLayout
import za.foundation.praekelt.mama.R

/**
 * Created by eduardokolomajr on 15/11/10.
 */
class BrowseAllActivity: AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_all)
        setSupportActionBar(this.simple_toolbar)
        setupNavigationDrawerWithUp(this.drawer_layout, this.nav_view, R.id.nav_browse_all)
    }
}