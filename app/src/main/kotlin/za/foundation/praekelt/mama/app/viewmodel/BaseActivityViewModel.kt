package za.foundation.praekelt.mama.app.viewmodel

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

/**
 * Created by eduardokolomajr on 2015/09/05.
 */
abstract class BaseActivityViewModel<T: Activity>(act: T): BaseViewModel(){
    var act: WeakReference<T>? = WeakReference(act)

    override fun onDestroy() {
        super.onDestroy()
        act?.clear()
        act = null
    }

    open fun onAttachActivity(activity: T){
        act = WeakReference(activity)
    }
}
