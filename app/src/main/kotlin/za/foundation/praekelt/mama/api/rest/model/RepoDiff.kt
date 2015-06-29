package za.foundation.praekelt.mama.api.rest.model

import java.util.*

/**
 * Diff of commits
 */
class RepoDiff(var currentIndex: String = "", var previousIndex: String = "",
               var name:String = "", var diffs: List<FormattedDiff> = ArrayList<FormattedDiff>()){
    companion object{
        val FIELD_CURRENT_INDEX: String = "current-index"
        val FIELD_PREVIOUS_INDEX: String = "previous-index"
        val FIELD_NAME: String = "name"
        val FIELD_DIFF: String = "diff"
    }
}
