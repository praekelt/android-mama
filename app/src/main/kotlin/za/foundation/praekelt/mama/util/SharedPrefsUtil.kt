package za.foundation.praekelt.mama.util

import android.content.Context
import org.jetbrains.anko.defaultSharedPreferences
import za.foundation.praekelt.mama.util.Constants as _C

public object SharedPrefsUtil{
    fun getCommitFromSharedPrefs (ctx: Context): String{
        return ctx.defaultSharedPreferences.getString(_C.SHARED_PREFS_COMMIT, "")
    }

    fun saveCommitToSharedPrefs (ctx: Context, commit: String){
        ctx.defaultSharedPreferences.edit().putString(_C.SHARED_PREFS_COMMIT, commit).apply()
    }
}
