package za.foundation.praekelt.mama.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.databinding.CategoryListItemBinding
import za.foundation.praekelt.mama.util.OrderBy
import za.foundation.praekelt.mama.util.otto.PageItemClickedPost

/**
 * Adapter class for list of stories per category
 * @see za.foundation.praekelt.mama.app.fragment.CategoryListFragment
 */
class CategoryListAdapter(var orderBy: OrderBy = OrderBy.POSITION,
                          var mPages: List<Page> = emptyList()) :
        RecyclerView.Adapter<CategoryListAdapter.ViewHolder>(), AnkoLogger {

    var pages: List<Page> = sortList(mPages)
        set(pgs) {
            val oldSize = field.size
            field = sortList(pgs)
            notifyRecyclerView(oldSize, field.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int): ViewHolder? {
        val view: View? = LayoutInflater.from(parent?.context).inflate(
                R.layout.category_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        //TODO empty list state
        return pages.size
    }


    override fun onBindViewHolder(holder: ViewHolder?, pos: Int) {
        holder!!
        holder.binding.page = pages[pos]
        holder.binding.root.visibility = View.VISIBLE
        holder.binding.root.onClick { v ->
            (v!!.context.applicationContext as App).bus.post(
                    PageItemClickedPost(pages[pos].uuid))
        }
        holder.binding.executePendingBindings()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val binding: CategoryListItemBinding = CategoryListItemBinding.bind(itemView)
    }

    fun sortList(pageList: List<Page>): List<Page> {
        when (orderBy) {
            OrderBy.POSITION -> return pageList.sortedBy { it.position }
            OrderBy.NAME -> return pageList.sortedBy { it.title }
        }
    }

    private fun notifyRecyclerView(oldSize: Int, newSize: Int) {
        if(newSize < oldSize){
            println("notifying shrinkage => $oldSize <=> $newSize; diff = ${oldSize-newSize}")
            notifyItemRangeRemoved(newSize, oldSize-newSize)
            notifyItemRangeChanged(0, newSize)
        }
        else if(newSize > oldSize){
            println("notifying expansion => $oldSize <=> $newSize; diff = ${newSize-oldSize}")
            notifyItemRangeChanged(0, oldSize)
            notifyItemRangeInserted(oldSize, newSize-oldSize)
        }else if(newSize == oldSize){
            notifyItemRangeChanged(0, oldSize)
        }
    }
}
