package za.foundation.praekelt.mama.app.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.api.model.Page_Table

/**
 * Created by eduardokolomajr on 2015/09/14.
 */
public class DetailPageActivityViewModel(val pageUuid: String): BaseObservable() {
    @Bindable val page: Page
    init{
        page = retrievePage();
    }

    fun retrievePage(): Page {
        val p = Select().from(javaClass<Page>()).where(Condition.column(Page_Table.UUID)
                .`is`(pageUuid)).querySingle()
        println("page title => ${p.getTitle()}")
        println("page content => ${p.getContent()}")
        return p
    }
}