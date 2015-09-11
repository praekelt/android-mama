package za.foundation.praekelt.mama.app

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.find
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Page
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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryListAdapter.ViewHolder? {
        val view: View? = LayoutInflater.from(parent?.getContext()).inflate(R.layout.category_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pages.size()
    }

    override fun onBindViewHolder(holder: ViewHolder?, pos: Int) {
        holder!!
        holder.resetView()
        holder.tv_title.setText(pages[pos].getTitle().trim())
        if(pages[pos].getTitle().trim() == pages[pos].getSubtitle()?.trim())
            holder.tv_subtitle.setVisibility(View.GONE)
        else
            holder.tv_subtitle.setText(pages[pos].getSubtitle()?.trim())

        if(pages[pos].getDescription().trim() == pages[pos].getSubtitle()?.trim())
            holder.tv_desc.setVisibility(View.GONE)
        else
            holder.tv_desc.setText(pages[pos].getDescription().trim())
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val tv_title: TextView
        val tv_subtitle: TextView
        val tv_desc: TextView

        init{
            tv_title = itemView!!.find<TextView>(R.id.tv_title_category_list_item)
            tv_subtitle = itemView.find<TextView>(R.id.tv_subtitle_category_list_item)
            tv_desc = itemView.find<TextView>(R.id.tv_desc_category_list_item)
        }

        fun resetView(): Boolean{
            tv_title.setVisibility(View.VISIBLE)
            tv_subtitle.setVisibility(View.VISIBLE)
            tv_desc.setVisibility(View.VISIBLE)
            return true
        }
    }

    fun sortList(pageList: MutableList<Page>): MutableList<Page>{
        Log.i("CLA", "sorting by ${orderBy}")
        when(orderBy){
            OrderBy.POSITION -> return pageList.sortBy(PagePositionComparator()) as MutableList
            OrderBy.NAME -> return pageList.sortBy(PageNameComparator()) as MutableList
            else -> return pageList
        }
    }
}