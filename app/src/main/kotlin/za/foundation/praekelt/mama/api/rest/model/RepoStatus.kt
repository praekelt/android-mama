package za.foundation.praekelt.mama.api.rest.model

import java.util.*

/**
 * Status of repository
 */
data class RepoStatus(var commit: String="", var name: String = "", var timestamp: Calendar = GregorianCalendar()){
    companion object{
        val FIELD_COMMIT = "commit"
        val FIELD_NAME = "name"
        val FIELD_TIMESTAMP = "timestamp"
    }
}
