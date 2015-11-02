package za.foundation.praekelt.mama.util

/**
 * Contains constants used throughout the app
 */
public object Constants {
//    const val BASE_URL:String = "http://172.30.88.49:6543"
    const val BASE_URL:String = "http://192.168.43.2:6543"
//    const val BASE_URL:String = "http://196.42.106.56:6543"
    const val REPO_NAME: String = "mama"

    const val REST_COMMIT_STUB: String = "commit-id"

    const val REMOTE_URL_EXT: String = ".json"
    const val REMOTE_REPOS_DIR: String = "/repos"
    const val REMOTE_STATUS_URL: String = "$REMOTE_REPOS_DIR/$REPO_NAME/status$REMOTE_URL_EXT"
    const val REMOTE_CLONE_URL: String = "$REMOTE_REPOS_DIR/$REPO_NAME/clone$REMOTE_URL_EXT"
    const val REMOTE_PULL_URL = "$REMOTE_REPOS_DIR/$REPO_NAME/pull/{$REST_COMMIT_STUB}$REMOTE_URL_EXT"
    const val REMOTE_DIFF_URL = "$REMOTE_REPOS_DIR/$REPO_NAME/diff/{$REST_COMMIT_STUB}$REMOTE_URL_EXT"

    const val REMOTE_DATE_FORMAT: String = "yyyy-MM-dd"

    const val FQN_LOCALISATION = "unicore.content.models.Localisation"
    const val FQN_CATEGORY = "unicore.content.models.Category"
    const val FQN_PAGE = "unicore.content.models.Page"

    const val SHARED_PREFS_COMMIT = "commit"
    const val SHARED_PREFS_LOCALE = "locale"
    const val SHARED_PREFS_LOCALE_DEFAULT = "eng_GB"
}