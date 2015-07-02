package za.foundation.praekelt.mama.api.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import za.foundation.praekelt.mama.api.rest.adapter.RepoAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoDiffAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoPullAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoStatusAdapter
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoDiff
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.api.rest.model.RepoStatus
import za.foundation.praekelt.mama.util.Constants

/**
 * Utils for the rest package
 */

fun createUCDServiceGson(): Gson{
    return GsonBuilder()
            .registerTypeAdapter(javaClass<RepoStatus>(), RepoStatusAdapter())
            .registerTypeAdapter(javaClass<Repo>(), RepoAdapter())
            .registerTypeAdapter(javaClass<RepoDiff>(), RepoDiffAdapter())
            .registerTypeAdapter(javaClass<RepoPull>(), RepoPullAdapter())
            .create()
}

fun createUCDService(gson: Gson = createUCDServiceGson()): RestAdapter{
    return RestAdapter.Builder()
            .setEndpoint(Constants.BASE_URL)
            .setConverter(GsonConverter(gson))
            .build()
}
