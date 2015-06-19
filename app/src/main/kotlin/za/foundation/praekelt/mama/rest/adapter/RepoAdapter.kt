package za.foundation.praekelt.mama.rest.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import za.foundation.praekelt.mama.rest.model.Repo
import za.foundation.praekelt.mama.util.Constants
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

/**
 * Created by eduardokolomajr on 2015/06/19.
 * Adapter for Repo class
 */

class RepoAdapter: TypeAdapter<Repo>(){
    override fun read(input: JsonReader?): Repo {
        val repo:Repo = Repo()

        if(input == null)
            throw IllegalArgumentException("Input is null!")

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                Repo.FIELD_AUTHOR -> repo.author = input.nextString()
                Repo.FIELD_COMMIT -> repo.commit = input.nextString()
                Repo.FIELD_NAME -> repo.name = input.nextString()
                Repo.FIELD_TIMESTAMP -> repo.timestamp
                        .setTime(SimpleDateFormat(Constants.REMOTE_DATE_FORMAT).parse(input.nextString().replace("T", " ")))
                else -> input.skipValue()
            }
        }
        input.endObject();
        return repo
    }

    override fun write(out: JsonWriter?, value: Repo?) {
        throw UnsupportedOperationException()
    }

}
