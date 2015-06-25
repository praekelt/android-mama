package za.foundation.praekelt.mama.rest.service

import retrofit.http.GET
import rx.Observable
import za.foundation.praekelt.mama.rest.model.RepoStatus
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * Created by eduardokolomajr on 2015/06/19.
 * unicore.distribute rest service client
 */

public interface UCDService{
    GET(_C.REMOTE_CLONE_URL)
    fun cloneRepo() : Observable<RepoStatus>
}


