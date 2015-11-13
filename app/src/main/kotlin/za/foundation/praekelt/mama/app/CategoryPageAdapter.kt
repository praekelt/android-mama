package za.foundation.praekelt.mama.app

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.app.fragment.CategoryListFragment
import za.foundation.praekelt.mama.app.fragment.EmptyListFragment
import za.foundation.praekelt.mama.util.OrderBy

/**
 * Adapter for category fragment view pager
 * @see za.foundation.praekelt.mama.app.activity.MainActivity
 */
class CategoryPageAdapter(val fm: FragmentManager, val locale: String,
                          var orderBy: OrderBy = OrderBy.POSITION,
                          mCategories: List<Category> = emptyList()) :
        FragmentStatePagerAdapter(fm) {
    var categories: List<Category> = sortList(mCategories)
        set(cats){
            field = sortList(cats)
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

    fun sortList(list: List<Category>): List<Category>{
        return when (orderBy) {
            OrderBy.POSITION -> list.sortedBy { it.position }
            OrderBy.NAME -> list.sortedBy { it.title }
            else -> list.sortedBy { it.position }
        }
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}

