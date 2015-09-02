package za.foundation.praekelt.mama.api.rest.model

import za.foundation.praekelt.mama.util.Constants as _C
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Localisation
import za.foundation.praekelt.mama.api.model.Page
import java.util.ArrayList

/**
 * Repo contains all files for each schema in unicore.distribute
 * server repo
 */

open class Repo(var commit: String = "", var pages: List<Page> = ArrayList<Page> (),
           var locales:List<Localisation> = ArrayList<Localisation>(),
           var categories:List<Category> = ArrayList<Category>()){
    companion object{
        val FIELD_COMMIT = "commit"
        val FIELD_CATEGORY = _C.FQN_CATEGORY
        val FIELD_LOCALISATION = _C.FQN_LOCALISATION
        val FIELD_PAGE = _C.FQN_PAGE
    }
}
