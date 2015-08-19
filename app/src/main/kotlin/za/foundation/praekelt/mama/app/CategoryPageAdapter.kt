package za.foundation.praekelt.mama.app

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.ViewGroup
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import rx.Observable
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Category_Table
import za.foundation.praekelt.mama.app.fragment.CategoryListFragment
import za.foundation.praekelt.mama.app.fragment.EmptyListFragment
import za.foundation.praekelt.mama.util.CategoryNameComparator
import za.foundation.praekelt.mama.util.CategoryPositionComparator
import za.foundation.praekelt.mama.util.OrderBy
import java.util.ArrayList

/**
 * Adapter for category fragment view pager
 * @see za.foundation.praekelt.mama.app.activity.MainActivity
 */
class CategoryPageAdapter(val fm: FragmentManager, val locale: String,
                          var orderBy: OrderBy = OrderBy.POSITION) :
        FragmentStatePagerAdapter(fm) {
    var categories: MutableList<Category> = ArrayList<Category>()

    init {
        refreshCategories().subscribe { notifyDataSetChanged() }
    }

    override fun getCount(): Int {
        return if (categories.isNotEmpty()) categories.size() else 1
    }

    override fun getItem(position: Int): Fragment? {
        return if (categories.isNotEmpty())
            CategoryListFragment(categories[position].getUuid(), locale)
        else {
            EmptyListFragment(R.string.empty_categories_message, R.drawable.ic_archive_accent_48dp)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (categories.isNotEmpty())
            categories[position].getTitle()
        else
            null
    }

    private fun refreshCategories(): Observable<Boolean> {
        return Observable.just(Select())
                .map { select ->
                    select.from(javaClass<Category>())
                            .where(Condition.column(Category_Table.FEATUREDINNAVBAR).`is`(true))
                            .and(Condition.column(Category_Table.LOCALEID).`is`(locale)).queryList()}
                .filter { list -> list != null }
                .map { list -> sortList(list) }
                .map { list ->
                    categories = list
                    Log.i("CPA", "list size = ${categories.size()}")
                    return@map categories.isNotEmpty()
                }
    }

    fun refresh(): Observable<Boolean> {
        return refreshCategories()
    }

    fun sortList(catList: MutableList<Category>): MutableList<Category> {
        when (orderBy) {
            OrderBy.POSITION -> return catList.sortBy(CategoryPositionComparator()) as MutableList
            OrderBy.NAME -> return catList.sortBy(CategoryNameComparator()) as MutableList
            else -> return catList
        }
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}

