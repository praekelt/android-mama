package za.foundation.praekelt.mama.app.viewmodel

import android.databinding.ObservableArrayList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.api.model.Page_Table
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.fragment.CategoryListFragment
import za.foundation.praekelt.mama.util.SharedPrefsUtil
import za.foundation.praekelt.mama.util.otto.ViewModelPost

/**
 * Created by eduardokolomajr on 2015/09/07.
 */
public class CategoryListFragmentViewModel(frag: CategoryListFragment) :
        BaseFragmentViewModel<CategoryListFragment>(frag), AnkoLogger {
    var layoutManager: RecyclerView.LayoutManager? = null
    var pages: ObservableArrayList<Page> = ObservableArrayList()
    var categoryUuid: String = ""
    var locale: String = ""

    companion object {
        const val TAG: String = "CategoryListFragmentViewModel"
    }

    override fun onAttachFragment(frag: CategoryListFragment) {
        super.onAttachFragment(frag)
        with(fragment?.get()!!){
            this@CategoryListFragmentViewModel.locale = SharedPrefsUtil.getLocale(ctx)
            layoutManager = LinearLayoutManager(ctx)
            categoryUuid = uuid
        }
        refreshList()
    }

    override fun onDestroy() {
        layoutManager = null
        (fragment?.get()?.act?.applicationContext as App).getApplicationComponent().bus()
                .post(ViewModelPost(CategoryListFragmentViewModel.TAG + categoryUuid, this))
        super.onDestroy()
    }

    fun refreshList(): Unit {
        with(pages){
            clear()
            async {
                Select().from(Page::class.java)
                    .where(Condition.column(Page_Table.PRIMARYCATEGORYID).`is`(categoryUuid))
                    .and(Condition.column(Page_Table.LOCALEID).`is`(locale))
                    .and(Condition.column(Page_Table.PUBLISHED).`is`(true)).queryList()
                .let{addAll(it)}
            }
        }
    }
}