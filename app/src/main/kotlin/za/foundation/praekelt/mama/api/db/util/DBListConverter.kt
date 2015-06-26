package za.foundation.praekelt.mama.api.db.util

import com.raizlabs.android.dbflow.converter.TypeConverter

/**
 * Converts contents of DBList between DB representation
 * and runtime class representation
 */

com.raizlabs.android.dbflow.annotation.TypeConverter
class DBListonverter: TypeConverter<String, DBStringList>() {
    override fun getDBValue(model: DBStringList?): String? {
        val sb:StringBuilder = StringBuilder()
        val iterator: MutableIterator<String>? = model?.iterator()
        while(iterator != null && iterator.hasNext()){
            sb.append(iterator.next())
            if(iterator.hasNext())
                sb.append(';')
        }
        return sb.toString()
    }

    override fun getModelValue(data: String?): DBStringList? {
        data?.split(";")
        val strings: DBStringList = DBStringList(data?.count { it -> it == ';' }?:-1 +1)
        return strings
    }

}
