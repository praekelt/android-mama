package za.foundation.praekelt.mama.app.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.fragment_category_list.rv_category_list
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.act
import rx.Observable
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.CategoryListAdapter

/**
 * Fragments for list of stories for a particular category
 */
class CategoryListFragment(var uuid: String = "", var locale: String = "", var position: Int = 0) : Fragment(), AnkoLogger {
    private object argsKeys {
        val uuidKey: String = "uuid"
        val localeKey: String = "locale"
        val positionKey = "position"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super<Fragment>.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater?.inflate(R.layout.fragment_category_list, container, false)!!
        Observable.just(savedInstanceState)
                .filter { it != null }
                .flatMap { Observable.from(it?.keySet()) }
                .subscribe { key ->
                    info("restoring frag $key")
                    when (key) {
                        argsKeys.uuidKey -> uuid = savedInstanceState!!.getString(key)
                        argsKeys.localeKey -> locale = savedInstanceState!!.getString(key)
                        argsKeys.positionKey -> position = savedInstanceState!!.getInt(key, 0)
                    }
                }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)
        this.rv_category_list.setLayoutManager(LinearLayoutManager(
                this@CategoryListFragment.act))

        val catListObs = Observable.just(CategoryListAdapter(uuid, locale))

        catListObs
                .flatMap { it.refresh() }
                .filter { it }
                .flatMap { catListObs }
                .subscribe { this.rv_category_list.setAdapter(it) }
    }

    override fun onResume() {
        super<Fragment>.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super<Fragment>.onSaveInstanceState(outState)
        outState?.putString(argsKeys.uuidKey, uuid)
        outState?.putString(argsKeys.localeKey, locale)
        outState?.putInt(argsKeys.positionKey,
                (this.rv_category_list.getLayoutManager() as LinearLayoutManager)
                        .findFirstVisibleItemPosition())
    }
}
