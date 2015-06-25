package za.foundation.praekelt.mama.rest.model

import java.util.*


/**
 * Created by eduardokolomajr on 2015/06/19.
 * Repository model class
 */

data class RepoStatus(var commit:String = "", var author:String = "",
                var name:String = "", var timestamp: Calendar = GregorianCalendar()){
    companion object{
        val FIELD_COMMIT:String = "commit"
        val FIELD_NAME:String = "name"
        val FIELD_TIMESTAMP:String = "timestamp"
    }
}