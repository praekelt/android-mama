package za.foundation.praekelt.mama.api.db;

import com.raizlabs.android.dbflow.annotation.Database;

import za.foundation.praekelt.mama.util.Constants;

/**
 * Application database object
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, generatedClassSeparator = "_")
public class AppDatabase {
    public static final String NAME = Constants.REPO_NAME;
    public static final int VERSION = 1;
}
