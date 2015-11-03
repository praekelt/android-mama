package za.foundation.praekelt.mama.app.fragment

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.fragment_empty_list.tv_empty_list
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import rx.Observable
import za.foundation.praekelt.mama.R

/**
 * Fragment to show on empty pager
 * requires message and logo to show
 */
class EmptyListFragment(@StringRes var message: Int = 0, @DrawableRes var image: Int = 0): Fragment(), AnkoLogger {
    private object argsKeys{
        val messageKey: String = "message"
        val imageKey: String = "img"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater?.inflate(R.layout.fragment_empty_list, container, false)!!

        savedInstanceState?.keySet()?.forEach {
            when (it) {
                argsKeys.messageKey -> message = savedInstanceState.getInt(it)
                argsKeys.imageKey -> image = savedInstanceState.getInt(it)
                else -> {}
            }
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("ELF", "create view start")
        this.tv_empty_list.setText(message)
        Log.i("ELF", "create view end")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(argsKeys.messageKey, message)
        outState?.putInt(argsKeys.imageKey, image)
    }
}
