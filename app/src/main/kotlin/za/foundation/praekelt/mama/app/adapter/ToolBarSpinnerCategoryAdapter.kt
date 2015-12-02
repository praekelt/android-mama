package za.foundation.praekelt.mama.app.adapter

import android.R
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.jetbrains.anko.find
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColor
import za.foundation.praekelt.mama.api.model.Category

class ToolBarSpinnerCategoryAdapter(val categories:List<Category>):
        BaseAdapter() {
    override fun getCount(): Int {
        return categories.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return createView(position, convertView, parent, true)
    }

    override fun getItem(position: Int): Category {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    fun createView(position: Int, convertView: View?, parent: ViewGroup?,
                   isDropDown: Boolean = false): View {
        return (convertView ?: parent!!.context.layoutInflater
                .inflate(za.foundation.praekelt.mama.R.layout.support_simple_spinner_dropdown_item, parent, false)).apply {
            with(find<TextView>(R.id.text1)){
                text = categories[position].title

                if(isDropDown)
                    textColor = context.getColor(za.foundation.praekelt.mama.R.color.colorPrimary)
            }
        }
    }
}