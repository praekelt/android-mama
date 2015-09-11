package za.foundation.praekelt.mama.app

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.databinding.CategoryListItemBinding
import za.foundation.praekelt.mama.util.OrderBy
import za.foundation.praekelt.mama.util.PageNameComparator
import za.foundation.praekelt.mama.util.PagePositionComparator
import java.util.ArrayList

/**
 * Adapter class for list of stories per category
 * @see za.foundation.praekelt.mama.app.fragment.CategoryListFragment
 */
class CategoryListAdapter(var orderBy: OrderBy = OrderBy.POSITION,
                          var pages: MutableList<Page> = ArrayList<Page>()) :
        RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {
    init {
        pages = sortList(pages)
    }

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int): CategoryListAdapter.ViewHolder? {
        val view: View? = LayoutInflater.from(parent?.getContext()).inflate(
                R.layout.category_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pages.size()
    }

    override fun onBindViewHolder(holder: ViewHolder?, pos: Int) {
        holder!!
        holder.binding.setPage(pages[pos])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val binding: CategoryListItemBinding = CategoryListItemBinding.bind(itemView)
    }

    fun sortList(pageList: MutableList<Page>): MutableList<Page> {
        Log.i("CLA", "sorting by ${orderBy}")
        when (orderBy) {
            OrderBy.POSITION -> return pageList.sortBy(PagePositionComparator()) as MutableList
            OrderBy.NAME -> return pageList.sortBy(PageNameComparator()) as MutableList
            else -> return pageList
        }
    }
}