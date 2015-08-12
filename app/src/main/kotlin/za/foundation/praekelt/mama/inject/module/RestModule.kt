package za.foundation.praekelt.mama.inject.module

import dagger.Module
import dagger.Provides
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.createUCDService
import za.foundation.praekelt.mama.inject.scope.ActivityScope

/**
 * Dagger module for rest services
 */
Module
class RestModule(){
    Provides
    ActivityScope
    fun provideUCDService(): UCDService {
        return createUCDService()
    }
}

