package za.foundation.praekelt.mama.inject.component

import dagger.Component
import za.foundation.praekelt.mama.app.fragment.CategoryListFragment
import za.foundation.praekelt.mama.inject.module.CategoryListFragmentModule

/**
 * Created by eduardokolomajr on 2015/09/10.
 */

@Component(modules = arrayOf(CategoryListFragmentModule::class))
public interface CategoryListFragmentComponent {
    fun inject(frag: CategoryListFragment): Unit
}