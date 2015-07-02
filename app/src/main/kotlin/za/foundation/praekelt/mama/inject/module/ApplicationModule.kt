package za.foundation.praekelt.mama.inject.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import za.foundation.praekelt.mama.api.rest.UCDService
import za.foundation.praekelt.mama.api.rest.adapter.RepoAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoDiffAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoPullAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoStatusAdapter
import za.foundation.praekelt.mama.api.rest.createUCDService
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoDiff
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.api.rest.model.RepoStatus
import za.foundation.praekelt.mama.app.App
import za.foundation.praekelt.mama.inject.scope.ApplicationScope
import za.foundation.praekelt.mama.util.Constants
import javax.inject.Singleton

/**
 * Dagger Application Module
 */
Module
class ApplicationModule(val app: App){
    Provides
    Singleton
    ApplicationScope
    fun provideApplicationContext(): Context{
        return app
    }
}
