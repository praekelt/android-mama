package za.foundation.praekelt.mama.app.activity

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.activity_browse_all.drawer_layout
import kotlinx.android.synthetic.activity_browse_all.nav_view
import kotlinx.android.synthetic.activity_browse_all.simple_toolbar
import kotlinx.android.synthetic.activity_browse_all.toolbar_category_spinner
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.find
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.app.viewmodel.BrowseAllActivityViewModel

/**
 * Created by eduardokolomajr on 15/11/10.
 */
class BrowseAllActivity: AppCompatActivity(), AnkoLogger {
    val viewModel: BrowseAllActivityViewModel = BrowseAllActivityViewModel(this);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_all)
        setSupportActionBar(this.simple_toolbar)
        setupNavigationDrawerWithUp(this.drawer_layout, this.nav_view, R.id.nav_browse_all)
        title = ""
        viewModel.onAttachActivity(this)
        this.toolbar_category_spinner.adapter = TSAdapter(viewModel.categories)
    }
}

class TSAdapter(val categories:List<Category>):
        BaseAdapter() {
    override fun getCount(): Int {
        return categories.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view:View = convertView ?: parent!!.context.layoutInflater
                .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false)

        view.find<TextView>(android.R.id.text1).text = categories[position].title
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view:View = convertView ?: parent!!.context.layoutInflater
                .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false)

        view.find<TextView>(android.R.id.text1).text = categories[position].title
        return view
    }

    override fun getItem(position: Int): Category {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

}