package za.foundation.praekelt.mama.app.viewmodel

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Category_Table
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.api.model.Page_Table
import za.foundation.praekelt.mama.app.activity.BrowseAllActivity
import za.foundation.praekelt.mama.util.SharedPrefsUtil

/**
 * Created by eduardokolomajr on 15/11/16.
 */
class BrowseAllActivityViewModel(act: BrowseAllActivity):
        BaseActivityViewModel<BrowseAllActivity>(act), AnkoLogger{
    val allCategories: Category = Category().apply { title = "All Categories" }
    var categories: ObservableList<Category> = ObservableArrayList()
    var pages: ObservableList<Page> = ObservableArrayList()
    var selectedCategory: Int = 0
        set(value){
            if(field != value) {
                field = value
                println("refreshing pages")
                refreshPages()
            }
        }
    lateinit var layoutManager: RecyclerView.LayoutManager

    companion object{
        @JvmField val TAG: String = BrowseAllActivityViewModel::class.javaClass.simpleName
    }

    override fun onAttachActivity(activity: BrowseAllActivity) {
        super.onAttachActivity(activity)
        layoutManager = MyLinearLayoutManager(activity)
        refreshCategories()
        refreshPages()
    }

    fun refreshCategories(){
        Select().from(Category::class.java)
                .where(Condition.column(Category_Table.LOCALEID)
                        .`is`(SharedPrefsUtil.getLocale(this.act!!.get())))
                .queryList()
                .sortedBy { it.title }
                .let { categories.addAll(listOf(allCategories)+it) }
    }

    private fun refreshPages() {
        with(Select().from(Page::class.java)){
            val where = where(Condition.column(Page_Table.LOCALEID)
                    .`is`(SharedPrefsUtil.getLocale((act!!.get()))))
                    .and(Condition.column(Page_Table.PUBLISHED).`is`(true))

            if(selectedCategory > 0)
                where.and(Condition.column(Page_Table.PRIMARYCATEGORYID)
                        .`is`(categories[selectedCategory].uuid))

            where.queryList()
            .sortedBy { it.position }
            .let { pages.clear(); pages.addAll(it); info { "pages collected => ${it.size}" } }
        }
    }

    class MyLinearLayoutManager(ctx: Context): LinearLayoutManager(ctx){
        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
}