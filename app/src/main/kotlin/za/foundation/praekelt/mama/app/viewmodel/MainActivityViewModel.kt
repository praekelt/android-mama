package za.foundation.praekelt.mama.app.viewmodel

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import kotlinx.android.synthetic.include_main_activity_view_pager.viewpager
import org.jetbrains.anko.AnkoLogger
import rx.Observable
import za.foundation.praekelt.mama.BR
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Category_Table
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.module.DaggerMainActivityViewModelComponent
import za.foundation.praekelt.mama.inject.component.MainActivityViewModelComponent
import za.foundation.praekelt.mama.inject.module.MainActivityViewModelModule
import za.foundation.praekelt.mama.util.otto.ViewModelPost
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * ViewModel for MainActivity
 * Current a problem with Dagger and this class, doesn't build factory classes
 */
public class MainActivityViewModel(mainActivity: MainActivity) :
        BaseActivityViewModel<MainActivity>(mainActivity), AnkoLogger {
    val viewModelComp: MainActivityViewModelComponent by Delegates.lazy { getViewModelComponent() }
    var repoObs: Observable<Repo> by Delegates.notNull()
        @Inject set
    var fm: FragmentManager?
    var app: App
    var adapter: CategoryPageAdapter? = null
    @Bindable var vp: ViewPager? = null
    @Bindable var categories: ObservableArrayList<Category> = ObservableArrayList()

    companion object{
        val TAG: String = "MainActivityViewModel"
    }

    init {
        println("init MAVM")
        fm = mainActivity.getSupportFragmentManager()
        app = mainActivity.appComp().app()
    }

    override fun onCreate() {
        super<BaseActivityViewModel>.onCreate()
        viewModelComp.inject(this);
        repoObs.doOnNext{ refreshCategories() }
                .doOnNext { Observable.interval(500, TimeUnit.MILLISECONDS).toBlocking().first() }
                .subscribe(
                { repo -> info("repo received")
                    notifyPropertyChanged(BR.vp) },
                { err -> info("error getting/updating repo") })
    }

    override fun onAttachActivity(activity: MainActivity) {
        info("start attach activity")
        super<BaseActivityViewModel>.onAttachActivity(activity)
        refreshCategories()
        vp = act?.get()?.viewpager
        fm = act?.get()?.getSupportFragmentManager()
        notifyPropertyChanged(BR.vp)
        info("end attach activity $vp vs ${act?.get()?.viewpager}")
    }

    override fun onDestroy() {
        fm = null
        vp = null
        act?.get()?.appComp()?.bus()?.post(ViewModelPost(TAG, this))
        super<BaseActivityViewModel>.onDestroy()
    }

    fun refreshCategories(): Unit {
        categories.clear()
        categories.addAll(Select().from(javaClass<Category>())
                .where(Condition.column(Category_Table.FEATUREDINNAVBAR).`is`(true))
                .and(Condition.column(Category_Table.LOCALEID).`is`(
                        _C.SHARED_PREFS_LOCALE_DEFAULT)).queryList())
    }

    fun getViewModelComponent(): MainActivityViewModelComponent {
        return DaggerMainActivityViewModelComponent.builder()
                .mainActivityViewModelModule(MainActivityViewModelModule(this))
                .build()
    }
}