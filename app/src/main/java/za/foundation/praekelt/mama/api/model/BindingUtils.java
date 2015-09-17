package za.foundation.praekelt.mama.api.model;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import za.foundation.praekelt.mama.app.CategoryListAdapter;
import za.foundation.praekelt.mama.app.CategoryPageAdapter;
import za.foundation.praekelt.mama.util.OrderBy;
import za.foundation.praekelt.mama.util.SharedPrefsUtil;

/**
 * Binding utils class for data binding library
 * Custom binds for layouts are located here
 */
public class BindingUtils {
    @BindingAdapter({"bind:viewPager", "bind:fm", "bind:category_items"})
    public static void setCategoryViewPager(TabLayout tl, ViewPager vp, FragmentManager fm, ObservableArrayList<Category> items) {
        if (vp.getAdapter() == null) {
            System.out.println("no vp adapter present");
            vp.setAdapter(new CategoryPageAdapter(fm,
                    SharedPrefsUtil.INSTANCE$.getLocale(tl.getContext()), OrderBy.POSITION, items));
        } else {
            System.out.println("refreshing vp adapter");
            ((CategoryPageAdapter) vp.getAdapter()).setCategories(items);
        }

        tl.setupWithViewPager(vp);
    }

    @BindingAdapter({"bind:pages"})
    public static void setPages(RecyclerView rv, ObservableArrayList<Page> pages) {
        if (rv.getAdapter() == null) {
            System.out.println("no rv adapter present");
            rv.setAdapter(new CategoryListAdapter(OrderBy.POSITION, pages));
        } else {
            System.out.println("refreshing vp adapter");
            ((CategoryListAdapter) rv.getAdapter()).setPages(pages);
        }
    }
}
