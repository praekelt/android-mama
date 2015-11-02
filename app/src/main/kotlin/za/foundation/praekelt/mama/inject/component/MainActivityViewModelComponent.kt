package za.foundation.praekelt.mama.inject.component

import dagger.Component
import za.foundation.praekelt.mama.app.viewmodel.MainActivityViewModel
import za.foundation.praekelt.mama.inject.module.MainActivityViewModelModule

/**
 * Created by eduardokolomajr on 2015/09/10.
 */
@Component(modules = arrayOf(MainActivityViewModelModule::class))
public interface MainActivityViewModelComponent{
    fun inject(viewModel: MainActivityViewModel): Unit
}