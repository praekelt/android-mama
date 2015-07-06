package za.foundation.praekelt.mama.api.rest.model

import za.foundation.praekelt.mama.api.rest.model.FormattedDiff.DiffType as DiffType

/**
 * Formatted diff
 */
data class FormattedDiff(var path:String = "", var type: DiffType = DiffType.NOT_SET){
    companion object{
        val FIELD_PATH = "path"
        val FIELD_TYPE = "type"
    }
    enum class DiffType(val type:Char){
        ADDED('A'),
        DELETED('D'),
        MODIFIED('M'),
        RENAMED('R'),
        NOT_SET('N')
    }
}