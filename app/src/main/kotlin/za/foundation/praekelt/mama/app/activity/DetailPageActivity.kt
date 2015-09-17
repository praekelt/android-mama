package za.foundation.praekelt.mama.app.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.viewmodel.DetailPageActivityViewModel
import za.foundation.praekelt.mama.databinding.ActivityDetailPageBinding
import za.foundation.praekelt.mama.inject.component.DaggerDetailPageActivityComponent
import za.foundation.praekelt.mama.inject.component.DetailPageActivityComponent
import za.foundation.praekelt.mama.inject.module.DetailPageActivityModule
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by eduardokolomajr on 2015/09/14.
 */
public class DetailPageActivity(): AppCompatActivity(), AnkoLogger{
    companion object{
        val TAG: String = "DetailPAgeActivity"
    }

    object argsKeys{
        val uuidKey = "pageUuid"
    }

    var pageUuid: String by Delegates.notNull()
        @Inject set
    val activityComp: DetailPageActivityComponent by Delegates.lazy { getDetailPageActivityComponent() }
    var viewModel: DetailPageActivityViewModel by Delegates.notNull()
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        activityComp.inject(this)
        info("after inject ${pageUuid}")
        val binding: ActivityDetailPageBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_page)
        binding.setPage(viewModel)
    }

    private fun getDetailPageActivityComponent(): DetailPageActivityComponent{
        return DaggerDetailPageActivityComponent.builder()
                .detailPageActivityModule(DetailPageActivityModule(this))
                .build()
    }
}
