package za.foundation.praekelt.mama.util

/**
 * Contains constants used throughout the app
 */
public object Constants {
    val BASE_URL:String = "http://172.30.88.49:6543"
    val REPO_NAME: String = "mama"

    val REST_COMMIT_STUB: String = "commit-id"

    val REMOTE_URL_EXT: String = ".json"
    val REMOTE_REPOS_DIR: String = "/repos"
    val REMOTE_STATUS_URL: String = "$REMOTE_REPOS_DIR/$REPO_NAME/status$REMOTE_URL_EXT"
    val REMOTE_CLONE_URL: String = "$REMOTE_REPOS_DIR/$REPO_NAME/clone$REMOTE_URL_EXT"
    val REMOTE_PULL_URL = "$REMOTE_REPOS_DIR/$REPO_NAME/pull/{$REST_COMMIT_STUB}$REMOTE_URL_EXT"
    val REMOTE_DIFF_URL = "$REMOTE_REPOS_DIR/$REPO_NAME/diff/{$REST_COMMIT_STUB}$REMOTE_URL_EXT"

    val REMOTE_DATE_FORMAT: String = "yyyy-MM-dd"

    val FQN_LOCALISATION = "unicore.content.models.Localisation"
    val FQN_CATEGORY = "unicore.content.models.Category"
    val FQN_PAGE = "unicore.content.models.Page"

    val SHARED_PREFS_COMMIT = "commit"
    val SHARED_PREFS_LOCALE = "locale"
    val SHARED_PREFS_LOCALE_DEFAULT = "eng_GB"
}