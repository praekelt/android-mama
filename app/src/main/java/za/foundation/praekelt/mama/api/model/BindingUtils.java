package za.foundation.praekelt.mama.api.model;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import za.foundation.praekelt.mama.app.CategoryPageAdapter;
import za.foundation.praekelt.mama.util.Constants;
import za.foundation.praekelt.mama.util.OrderBy;

/**
 * Binding utils class for data binding library
 * Custom binds for layouts are located here
 */
public class BindingUtils {
    @BindingAdapter({"bind:viewPager", "bind:fm", "bind:category_items"})
    public static void setCategoryViewPager(TabLayout tl, ViewPager vp, FragmentManager fm, ObservableArrayList<Category> items) {
        System.out.println("binding vp");
        if (vp.getAdapter() == null) {
            System.out.println("no adapter present");
            vp.setAdapter(new CategoryPageAdapter(fm,
                    Constants.SHARED_PREFS_LOCALE_DEFAULT, OrderBy.POSITION, items));
        } else {
            System.out.println("refreshing adapter");
            ((CategoryPageAdapter) vp.getAdapter()).setCategories(items);
        }

        tl.setupWithViewPager(vp);
    }
}
