package za.foundation.praekelt.mama.util

/**
 * Contains constants used throughout the app
 * Created by eduardokolomajr on 2015/06/17.
 */
class Constants{
    companion object{
        val BASE_URL:String = "http://172.30.88.49:6543"
        val REPO_NAME:String = "mama"

        val REMOTE_URL_EXT:String = ".json"
        val REMOTE_REPOS_DIR:String = "repos"
        val REMOTE_STATUS_URL:String = "/status/$REPO_NAME$REMOTE_URL_EXT"
        val REMOTE_CLONE_URL:String = "/clone/$REPO_NAME$REMOTE_URL_EXT"
        val REMOTE_PULL_URL = "/pull/$REPO_NAME/"
        val REMOTE_DIFF_URL = "/status/$REPO_NAME/"


        val REMOTE_DATE_FORMAT:String = "yyyy-MM-dd HH:mm:ss"
    }
}