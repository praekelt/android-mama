package za.foundation.praekelt.mama.api.rest

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit.RestAdapter
import retrofit.client.Client
import retrofit.client.Request
import retrofit.client.Response
import retrofit.converter.GsonConverter
import retrofit.mime.TypedByteArray
import za.foundation.praekelt.mama.api.rest.adapter.RepoAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoDiffAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoPullAdapter
import za.foundation.praekelt.mama.api.rest.adapter.RepoStatusAdapter
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoDiff
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.api.rest.model.RepoStatus
import za.foundation.praekelt.mama.util.Constants as _C
import java.io.File
import java.util.Collections
import kotlin.text.Regex

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
            .setEndpoint(_C.BASE_URL)
            .setConverter(GsonConverter(gson))
            .build()
}

internal fun createTestUCDService(gson: Gson = createUCDServiceGson()): RestAdapter {
    return RestAdapter.Builder()
            .setEndpoint(_C.BASE_URL)
            .setConverter(GsonConverter(gson))
            .setClient(TestHttpClient())
            .build()
}

class TestHttpClient: Client {
    val diffSubstring:String = "${_C.REMOTE_REPOS_DIR}/${_C.REPO_NAME}/diff/"
    val pullSubstring: String = "${_C.REMOTE_REPOS_DIR}/${_C.REPO_NAME}/pull/"
    override fun execute(request: Request?): Response? {
        if (request == null)
            throw NullPointerException("request is null")

        val uri: Uri = Uri.parse(request.getUrl())
        when{
            uri.getPath().equals(_C.REMOTE_STATUS_URL) -> return returnStatusResponse(request)
            uri.getPath().equals(_C.REMOTE_CLONE_URL) -> return returnCloneRepoResponse(request)
            uri.getPath().contains(diffSubstring) -> return returnDiffRepoRequest(request)
            uri.getPath().contains(pullSubstring) -> return returnPullRepoRequest(request)
            else -> {
                return Response(request.getUrl(), 404, "Not found", Collections.emptyList(),
                        TypedByteArray("application/json", "{}".toByteArray()))
            }
        }
    }

    fun returnStatusResponse(request: Request): Response{
        val status: String = File("src/test/testFiles/testStatus.json").readText()
        return Response(request.getUrl(), 200, "OK",
                Collections.emptyList(), TypedByteArray("application/json", status.toByteArray()))
    }

    fun returnCloneRepoResponse(request: Request): Response{
        val repo: String = File("src/test/testFiles/testRepo.json").readText()
        return Response(request.getUrl(), 200, "OK",
                Collections.emptyList(), TypedByteArray("application/json", repo.toByteArray()))
    }

    fun returnDiffRepoRequest(request: Request): Response{
        val status: String = File("src/test/testFiles/testDiff.json").readText()
        return Response(request.getUrl(), 200, "OK",
                Collections.emptyList(), TypedByteArray("application/json", status.toByteArray()))
    }

    fun returnPullRepoRequest(request: Request): Response{
        val status: String = File("src/test/testFiles/testPull.json").readText()
        return Response(request.getUrl(), 200, "OK",
                Collections.emptyList(), TypedByteArray("application/json", status.toByteArray()))
    }
}