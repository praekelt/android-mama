package za.foundation.praekelt.mama.api.rest.adapter

import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import za.foundation.praekelt.mama.api.db.util.DBStringList
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Localisation
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.api.rest.model.Repo
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.GregorianCalendar
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * JSON Adapter for the Repo class
 */
class RepoAdapter: TypeAdapter<Repo>() {
    override fun read(input: JsonReader?): Repo {
        if(input == null)
            throw IllegalArgumentException("Input is null!")

        var repo:Repo = Repo()

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                Repo.FIELD_COMMIT -> repo.commit = input.nextString()
                Repo.FIELD_PAGE -> repo.pages = processPages(input)
                Repo.FIELD_LOCALISATION -> repo.locales = processLocales(input)
                Repo.FIELD_CATEGORY -> repo.categories = processCategories(input)
                else -> {
                    Log.d("RepoAdapter", "read found unknown tag => ${input.nextName()}")
                    input.skipValue()
                }
            }
        }
        input.endObject()
        return repo
    }

    override fun write(out: JsonWriter?, value: Repo?) {
        throw UnsupportedOperationException()
    }
}