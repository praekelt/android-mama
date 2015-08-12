package za.foundation.praekelt.mama.app.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.act
import org.jetbrains.anko.find
import rx.Observable
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.CategoryListAdapter

/**
 * Fragments for list of stories for a particular category
 */
class CategoryListFragment(val uuid: String, val locale: String): Fragment(), AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super<Fragment>.onCreateView(inflater, container, savedInstanceState)
        val view:View = inflater?.inflate(R.layout.fragment_category_list, container, false)!!
        val recyclerView = view.find<RecyclerView>(R.id.rv_category_list)
        recyclerView.setLayoutManager(LinearLayoutManager(
                            this@CategoryListFragment.act))
        val catListObs = Observable.just(CategoryListAdapter(uuid, locale))

        catListObs
                .flatMap{it.refresh()}
                .filter{it}
                .flatMap{catListObs}
                .subscribe{
                    recyclerView.setAdapter(it)
                }
        return view
    }

    override fun onResume() {
        super<Fragment>.onResume()
    }
}
