package za.foundation.praekelt.mama.api.rest

import retrofit.http.GET
import retrofit.http.Path
import rx.Observable
import za.foundation.praekelt.mama.api.rest.model.Repo
import za.foundation.praekelt.mama.api.rest.model.RepoDiff
import za.foundation.praekelt.mama.api.rest.model.RepoPull
import za.foundation.praekelt.mama.api.rest.model.RepoStatus
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * REST service class that interfaces with the unicore.distribute server
 */

interface UCDService{
    GET(_C.REMOTE_STATUS_URL)
    fun getRepoStatus(): Observable<RepoStatus>

    GET(_C.REMOTE_CLONE_URL)
    fun cloneRepo(): Observable<Repo>

    GET(_C.REMOTE_DIFF_URL)
    fun getRepoDiff(Path(_C.REST_COMMIT_STUB) commitId:String): Observable<RepoDiff>

    GET(_C.REMOTE_PULL_URL)
    fun pullRepo(Path(_C.REST_COMMIT_STUB) commitId:String): Observable<RepoPull>
}
