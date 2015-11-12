package za.foundation.praekelt.mama.app.viewmodel

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import kotlinx.android.synthetic.include_main_activity_view_pager.viewpager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.async
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import za.foundation.praekelt.mama.BR
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Category_Table
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.component.DaggerMainActivityViewModelComponent
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
    val viewModelComp: MainActivityViewModelComponent by lazy { getViewModelComponent() }
    lateinit var repoObs: Observable<Repo>
        @Inject set
    var fm: FragmentManager? = null
    var app: App
    @Bindable var vp: ViewPager? = null
    @Bindable var categories: ObservableArrayList<Category> = ObservableArrayList()

    companion object{
        const val TAG: String = "MainActivityViewModel"
    }

    init {
        app = mainActivity.activityComp.app()
        viewModelComp.inject(this);
        repoObs.subscribe ( { repo -> info("got repo"); refreshCategories() }, { err -> print("error getting repo")})
    }

    override fun onAttachActivity(activity: MainActivity) {
//        info("start attach activity")
        super.onAttachActivity(activity)
        refreshCategories()
        with(act?.get()!!){
            vp = viewpager
            fm = supportFragmentManager
            notifyPropertyChanged(BR.vp)
        }
    }

    override fun onDestroy() {
        fm = null
        vp = null
        act?.get()?.activityComp?.bus()?.post(ViewModelPost(TAG, this))
        super.onDestroy()
    }

    fun refreshCategories(): Unit {
        with(categories){
            clear()
            async {
                Select().from(Category::class.java)
                    .where(Condition.column(Category_Table.FEATUREDINNAVBAR).`is`(true))
                    .and(Condition.column(Category_Table.LOCALEID).`is`(
                            _C.SHARED_PREFS_LOCALE_DEFAULT)).queryList()
                    .let{ addAll(it) }
            }
        }
    }

    fun getViewModelComponent(): MainActivityViewModelComponent {
        return DaggerMainActivityViewModelComponent.builder()
                .mainActivityViewModelModule(MainActivityViewModelModule(this))
                .build()
    }
}