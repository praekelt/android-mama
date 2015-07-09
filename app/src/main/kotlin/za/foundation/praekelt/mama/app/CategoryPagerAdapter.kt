package za.foundation.praekelt.mama.app

import android.database.DataSetObserver
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Category_Table
import za.foundation.praekelt.mama.app.fragment.MainActivityFragment
import java.util.ArrayList

/**
 * Adapter for category fragment view pager
 * @see za.foundation.praekelt.mama.app.activity.MainActivity
 */
class CategoryPageAdapter(val fm: FragmentManager): FragmentPagerAdapter(fm) {
    init{
        refreshCategories().subscribe{}
    }

    var categories: MutableList<Category> = ArrayList<Category>()

    override fun getCount(): Int {
        return categories.size()
    }

    override fun getItem(position: Int): Fragment? {
        return MainActivityFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return categories[position].getTitle()
    }

    private fun refreshCategories(): Observable<Boolean> {
        return Observable.just(Select())
                .map{select ->
                    select.from(javaClass<Category>())
                        .where(Condition.column(Category_Table.FEATUREDINNAVBAR)
                        .`is`(true)).queryList()}
                .filter{list -> list != null}
                .map{list ->
                    categories = list
                    Log.i("CPA", "list size = ${categories.size()}")
                    return@map categories.isNotEmpty()
                }
    }

    fun refresh(): Observable<Boolean>{
        Log.i("CPA", "refreshing")
        return refreshCategories()
    }
}

