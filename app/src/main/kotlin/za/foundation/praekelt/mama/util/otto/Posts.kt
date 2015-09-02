package za.foundation.praekelt.mama.util.otto

import rx.Observable
import rx.functions.Action
import rx.functions.Function

/**
 * Created by eduardokolomajr on 2015/08/27.
 */

open data class RxPost<T>(final val first: String, final val second: List<T>)
data class ObservablePost(first: String, second: List<Observable<out Any>>): RxPost<Observable<out Any>>(first, second)
data class FunctionPost(first: String, second: List<Function>): RxPost<Function>(first, second)
data class ActionPost(first: String, second: List<Action>): RxPost<Action>(first, second)
