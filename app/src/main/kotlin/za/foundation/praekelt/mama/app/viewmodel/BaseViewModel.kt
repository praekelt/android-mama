package za.foundation.praekelt.mama.app.viewmodel

import android.databinding.BaseObservable

/**
 * Created by eduardokolomajr on 2015/09/05.
 */
public abstract class BaseViewModel(): BaseObservable() {
    open fun onCreate(){}

    open fun onDestroy(){}
}