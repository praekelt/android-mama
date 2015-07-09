package za.foundation.praekelt.mama.inject.module

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.util.Log
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.defaultSharedPreferences
import za.foundation.praekelt.mama.app.CategoryPageAdapter
import za.foundation.praekelt.mama.app.activity.MainActivity
import za.foundation.praekelt.mama.inject.scope.ActivityScope
import kotlin.properties.Delegates
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * Dagger module for main activity
 */
Module
class MainActivityModule(val activity: MainActivity) {
    Provides
    ActivityScope
    fun provideActivityContext(): Context {
        return activity
    }

    Provides
    ActivityScope
    fun provideNav(): NavigationView {
        activity.navigationView.setNavigationItemSelectedListener {
            menuItem ->
            menuItem.setChecked(true)
            activity.mDrawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
        return activity.navigationView
    }

    Provides
    ActivityScope
    fun provideFragmentManager(): FragmentManager{
        return activity.getSupportFragmentManager()
    }

    Provides
    ActivityScope
    fun provideCategoryPageAdapter(fm: FragmentManager): CategoryPageAdapter{
        val locale:String = activity.defaultSharedPreferences
                .getString(_C.SHARED_PREFS_LOCALE, _C.SHARED_PREFS_LOCALE_DEFAULT)
        return CategoryPageAdapter(fm, locale)
    }

    Provides
    ActivityScope
    fun provideViewPager(categoryPagerAdapter: CategoryPageAdapter): ViewPager {
        categoryPagerAdapter.refresh().subscribe{ }
        activity.viewPager.setAdapter(categoryPagerAdapter)
        return activity.viewPager
    }

    Provides
    ActivityScope
    fun provideTabLayout(viewPager: ViewPager): TabLayout {
        activity.tabLayout.setupWithViewPager(viewPager)
        return activity.tabLayout
    }
}