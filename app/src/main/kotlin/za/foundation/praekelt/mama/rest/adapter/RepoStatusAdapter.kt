package za.foundation.praekelt.mama.rest.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import za.foundation.praekelt.mama.rest.model.RepoStatus
import za.foundation.praekelt.mama.util.Constants
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

/**
 * Created by eduardokolomajr on 2015/06/19.
 * Adapter for RepoStatus class
 */

class RepoStatusAdapter: TypeAdapter<RepoStatus>(){
    override fun read(input: JsonReader?): RepoStatus {
        val repoStatus:RepoStatus = RepoStatus()

        if(input == null)
            throw IllegalArgumentException("Input is null!")

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                RepoStatus.FIELD_COMMIT -> repoStatus.commit = input.nextString()
                RepoStatus.FIELD_NAME -> repoStatus.name = input.nextString()
                RepoStatus.FIELD_TIMESTAMP -> repoStatus.timestamp
                        .setTime(SimpleDateFormat(Constants.REMOTE_DATE_FORMAT).parse(input.nextString().replace("T", " ")))
                else -> input.skipValue()
            }
        }
        input.endObject();
        return repoStatus
    }

    override fun write(out: JsonWriter?, value: RepoStatus?) {
        throw UnsupportedOperationException()
    }

}
