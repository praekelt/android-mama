package za.foundation.praekelt.mama.rest.service

import retrofit.http.GET
import rx.Observable
import za.foundation.praekelt.mama.rest.model.Repo
import za.foundation.praekelt.mama.util.Constants

/**
 * Created by eduardokolomajr on 2015/06/19.
 * unicore.distribute rest service client
 */

public interface UCDService{
    GET("/${Constants.REMOTE_REPOS_DIR}/${Constants.REPO_NAME}.json")
    fun getRepoStatus() : Observable<Repo>
}


