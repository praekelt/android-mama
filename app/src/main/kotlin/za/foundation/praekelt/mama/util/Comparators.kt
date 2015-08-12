package za.foundation.praekelt.mama.util

import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Page
import java.util.Comparator

/**
 * Category comparators
 */

enum class OrderBy{
    POSITION,
    NAME
}

interface  CategoryComparator: Comparator<Category>{
    fun checkForNull(p0: Category?, p1: Category?):Unit{
        if(p0 == null)
            throw NullPointerException("Argument 1 is null")

        if(p1 == null)
            throw NullPointerException("Argument 2 is null")
    }
}

class CategoryPositionComparator: CategoryComparator{
    override fun compare(p0: Category?, p1: Category?): Int {
        checkForNull(p0, p1)
        return p0!!.getPosition().compareTo(p1!!.getPosition())
    }
}

class CategoryNameComparator: CategoryComparator{
    override fun compare(p0: Category?, p1: Category?): Int {
        checkForNull(p0, p1)
        return p0!!.getTitle().compareTo(p1!!.getTitle())
    }

}

interface PageComparator: Comparator<Page>{
    fun checkForNull(p0: Page?, p1: Page?):Unit{
        if(p0 == null)
            throw NullPointerException("Argument 1 is null")

        if(p1 == null)
            throw NullPointerException("Argument 2 is null")
    }
}

class PagePositionComparator: PageComparator{
    override fun compare(p0: Page?, p1: Page?): Int {
        checkForNull(p0, p1)
        return p0!!.getPosition().compareTo(p1!!.getPosition())
    }
}

class PageNameComparator: PageComparator{
    override fun compare(p0: Page?, p1: Page?): Int {
        checkForNull(p0, p1)
        return p0!!.getTitle().compareTo(p1!!.getTitle())
    }
}
