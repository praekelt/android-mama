@file:JvmName("AppCompatActivityUtils")
package za.foundation.praekelt.mama.app.activity

import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.support.v4.drawerLayout
import org.jetbrains.anko.toast
import za.foundation.praekelt.mama.R

/**
 * Extension methods for AppCompatActivity class
 */

private fun AppCompatActivity.setupBaseNavigationDrawer(currentItem: Int, drawerLayout: DrawerLayout, navView: NavigationView) {
    with(navView) {
        setCheckedItem(currentItem)
        setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                currentItem -> {
                    drawerLayout.closeDrawers()
                    return@setNavigationItemSelectedListener false
                }
                R.id.nav_featured_stories -> startActivity(intentFor<MainActivity>().clearTask())
                R.id.nav_browse_all -> startActivity(intentFor<BrowseAllActivity>().singleTop())
                R.id.nav_bookmarks -> toast("Under construction")
            }

            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }
}

fun AppCompatActivity.setupNavigationDrawer(drawerLayout: DrawerLayout, navView: NavigationView,
                                            @IdRes currentItem:Int,
                                            toolbar: Toolbar): ActionBarDrawerToggle{
    setupBaseNavigationDrawer(currentItem, drawerLayout, navView)

    //Setup hamburger icon on navigation view
    val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.nav_open_desc, R.string.nav_closed_desc)
    drawerLayout.setDrawerListener(actionBarDrawerToggle)
    actionBarDrawerToggle.syncState()
    return actionBarDrawerToggle
}

fun AppCompatActivity.setupNavigationDrawerWithUp(drawerLayout: DrawerLayout, navView: NavigationView,
                                                  @IdRes currentItem:Int){
    setupBaseNavigationDrawer(currentItem, drawerLayout, navView)

    with(supportActionBar){
        setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        setDisplayHomeAsUpEnabled(true)
    }
}