package za.foundation.praekelt.mama.api.rest.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import za.foundation.praekelt.mama.api.rest.model.FormattedDiff
import za.foundation.praekelt.mama.api.rest.model.RepoPull

/**
 * JSON Adapter for the RepoPull class
 */
class RepoPullAdapter: TypeAdapter<RepoPull>(){
    override fun read(input: JsonReader?): RepoPull {
        if(input == null)
            throw IllegalArgumentException("input is null")

        var repoPull:RepoPull = RepoPull()

        input.beginObject()
        while(input.hasNext()){
            when(input.nextName()){
                RepoPull.FIELD_COMMIT -> repoPull.commit = input.nextString()
                RepoPull.FIELD_CATEGORY -> repoPull.categories = processCategories(input)
                RepoPull.FIELD_LOCALISATION -> repoPull.locales = processLocales(input)
                RepoPull.FIELD_PAGE -> repoPull.pages = processPages(input)
                RepoPull.FIELD_OTHER -> repoPull.diffs = processDiffs(input)
            }
        }
        input.endObject()
        return repoPull
    }

    override fun write(out: JsonWriter?, value: RepoPull?) {
        throw UnsupportedOperationException()
    }
}