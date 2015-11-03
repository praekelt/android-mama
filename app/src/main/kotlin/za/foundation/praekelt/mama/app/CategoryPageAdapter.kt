package za.foundation.praekelt.mama.app

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Category
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
                          var orderBy: OrderBy = OrderBy.POSITION,
                          mCategories: MutableList<Category> = ArrayList<Category>()) :
        FragmentPagerAdapter(fm) {
    var categories: MutableList<Category> = sortList(mCategories)
        set(cats){
            field = cats
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return if (categories.isNotEmpty()) categories.size else 1
    }

    override fun getItem(position: Int): Fragment? {
        return if (categories.isNotEmpty())
            CategoryListFragment(categories[position].uuid, locale)
        else {
            EmptyListFragment(R.string.empty_categories_message, R.drawable.ic_archive_accent_48dp)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (categories.isNotEmpty())
            categories[position].title
        else
            null
    }

    fun sortList(catList: MutableList<Category>): MutableList<Category> {
        when (orderBy) {
            OrderBy.POSITION -> return catList.sortedWith(CategoryPositionComparator()) as MutableList
            OrderBy.NAME -> return catList.sortedWith(CategoryNameComparator()) as MutableList
            else -> return catList
        }
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}

