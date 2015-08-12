package za.foundation.praekelt.mama.api.db

import com.raizlabs.android.dbflow.annotation.Database
import za.foundation.praekelt.mama.util.Constants as _C

/**
 * Application database object
 */

Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, generatedClassSeparator = "_")
public object AppDatabase{
    public val NAME: String = _C.REPO_NAME
    public val VERSION: Int = 1
}