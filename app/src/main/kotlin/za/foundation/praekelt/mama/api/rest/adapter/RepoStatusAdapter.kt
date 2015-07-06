package za.foundation.praekelt.mama.api.rest.adapter

import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import za.foundation.praekelt.mama.api.rest.model.RepoStatus

/**
 * JSON adapter for RepoStatus class
 */

class RepoStatusAdapter: TypeAdapter<RepoStatus>(){
    override fun read(input: JsonReader?): RepoStatus {
        if(input == null)
            throw IllegalArgumentException("Input is null!")

        var repoStatus:RepoStatus = RepoStatus()

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                RepoStatus.FIELD_COMMIT -> repoStatus.commit = input.nextString()
                RepoStatus.FIELD_NAME -> repoStatus.name = input.nextString()
                RepoStatus.FIELD_TIMESTAMP -> repoStatus.timestamp = processDate(input.nextString())
                else -> {
                    Log.d("RepoStatusAdapter", "read found unknown tag")
                    input.skipValue()
                }
            }
        }
        input.endObject();
        return repoStatus
    }

    override fun write(out: JsonWriter?, value: RepoStatus?) {
        throw UnsupportedOperationException()
    }

}
