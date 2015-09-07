package za.foundation.praekelt.mama.util.otto

import android.app.Activity
import rx.Observable
import rx.functions.Action
import rx.functions.Function
import za.foundation.praekelt.mama.app.viewmodel.BaseActivityViewModel

/**
 * Classes used to post events using otto
 */

open data class RxPost<T>(final val first: String, final val second: T)
data class ActivityViewModelPost(first: String, second: BaseActivityViewModel<out Activity>):
        RxPost<BaseActivityViewModel<out Activity>>(first, second)