package za.foundation.praekelt.mama.app.viewmodel

import android.databinding.ObservableArrayList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
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
    var position: Int = 0
    var offset: Int = 0

    companion object {
        val TAG: String = "CategoryListFragmentViewModel"
    }

    init {

        //        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, offset)
        //        categoryUuid = frag.uuid
        //        info("uuid => ${categoryUuid}")
    }

    override fun onAttachFragment(frag: CategoryListFragment) {
        super<BaseFragmentViewModel>.onAttachFragment(frag)
        locale = SharedPrefsUtil.getLocale(frag.ctx)
        layoutManager = LinearLayoutManager(frag.ctx)
        categoryUuid = frag.uuid
        position = frag.position
        offset = frag.offset
        //        info("$categoryUuid VM pos"+position)
        //        info("$categoryUuid VM off"+offset)
        refreshList()
    }

    override fun onDestroy() {
        layoutManager = null
        (fragment?.get()?.act?.getApplicationContext() as App).getApplicationComponent().bus()
                .post(ViewModelPost(CategoryListFragmentViewModel.TAG + categoryUuid, this))
        super<BaseFragmentViewModel>.onDestroy()
    }

    fun refreshList(): Unit {
        //        info("refreshing list for cat")
        pages.clear()
        pages.addAll(Select().from(javaClass<Page>())
                .where(Condition.column(Page_Table.PRIMARYCATEGORYID).`is`(categoryUuid))
                .and(Condition.column(Page_Table.LOCALEID).`is`(locale))
                .and(Condition.column(Page_Table.PUBLISHED).`is`(true)).queryList())
        //        info("pages length ${pages.size()}")
    }
}