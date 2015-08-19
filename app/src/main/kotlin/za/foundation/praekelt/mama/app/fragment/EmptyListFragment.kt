package za.foundation.praekelt.mama.app.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
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
        super<Fragment>.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater?.inflate(R.layout.fragment_empty_list, container, false)!!
        Observable.just(savedInstanceState)
            .filter{ it != null }
            .flatMap{ Observable.from(it?.keySet()) }
            .subscribe{ key ->
                Log.i("ELF", "restoring frag $key")
                when (key) {
                    argsKeys.messageKey -> message = savedInstanceState!!.getInt(key)
                    argsKeys.imageKey -> image = savedInstanceState!!.getInt(key)
                }
            }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)
        Log.i("ELF", "create view start")
        this.tv_empty_list.setText(message)
        Log.i("ELF", "create view end")
    }

    override fun onResume() {
        super<Fragment>.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super<Fragment>.onSaveInstanceState(outState)
        outState?.putInt(argsKeys.messageKey, message)
        outState?.putInt(argsKeys.imageKey, image)
    }
}
