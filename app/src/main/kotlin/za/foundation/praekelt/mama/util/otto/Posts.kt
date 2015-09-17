package za.foundation.praekelt.mama.util.otto

import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.app.viewmodel.BaseViewModel

/**
 * Classes used to post events using otto
 */

open data class RxPost<T>(final val first: String, final val second: T)

data class ViewModelPost(first: String, second: BaseViewModel) :
        RxPost<BaseViewModel>(first, second)

data class PageItemClickedPost(val pageUuid: String)