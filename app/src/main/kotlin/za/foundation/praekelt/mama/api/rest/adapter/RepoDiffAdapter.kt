package za.foundation.praekelt.mama.api.rest.adapter

import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import za.foundation.praekelt.mama.api.rest.model.RepoDiff

/**
 * JSON adapter for RepoDiff class
 */

class RepoDiffAdapter: TypeAdapter<RepoDiff>() {
    override fun read(input: JsonReader?): RepoDiff {
        if(input == null)
            throw IllegalArgumentException("Input is null!")

        var repoDiff:RepoDiff = RepoDiff()

        input.beginObject()
        while(input.hasNext()){
            when(input.nextName()){
                RepoDiff.FIELD_NAME -> repoDiff.name = input.nextString()
                RepoDiff.FIELD_CURRENT_INDEX ->repoDiff.currentIndex = input.nextString()
                RepoDiff.FIELD_PREVIOUS_INDEX -> repoDiff.previousIndex = input.nextString()
                RepoDiff.FIELD_DIFF -> repoDiff.diffs = processDiffs(input)
                else -> {
                    Log.d("RepoDiffAdapter", "read found unknown tag")
                    input.skipValue()
                }
            }
        }
        input.endObject()
        return repoDiff
    }

    override fun write(out: JsonWriter?, value: RepoDiff?) {
        throw UnsupportedOperationException()
    }
}
