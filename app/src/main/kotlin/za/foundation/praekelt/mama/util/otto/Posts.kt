package za.foundation.praekelt.mama.util.otto

import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.app.viewmodel.BaseViewModel

/**
 * Classes used to post events using otto
 */

open class RxPost<T>(final val first: String, final val second: T)

class ViewModelPost(first: String, second: BaseViewModel) :
        RxPost<BaseViewModel>(first, second)

class PageItemClickedPost(val pageUuid: String)