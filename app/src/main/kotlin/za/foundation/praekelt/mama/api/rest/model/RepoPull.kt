package za.foundation.praekelt.mama.api.rest.model

import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Localisation
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.util.Constants as _C
import java.util.ArrayList

/**
 * Repository pull request
 */

class RepoPull(commit: String = "",  pages: List<Page> = ArrayList<Page> (),
               locales: List<Localisation> = ArrayList<Localisation> (),
               categories: List<Category> = ArrayList<Category> (),
               var diffs: List<FormattedDiff> = ArrayList<FormattedDiff> ()):
        Repo(commit, pages, locales, categories){
    companion object{
        val FIELD_COMMIT = "commit"
        val FIELD_OTHER = "other"
        val FIELD_CATEGORY = _C.FQN_CATEGORY
        val FIELD_LOCALISATION = _C.FQN_LOCALISATION
        val FIELD_PAGE = _C.FQN_PAGE
    }
}