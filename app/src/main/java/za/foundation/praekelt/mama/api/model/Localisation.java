package za.foundation.praekelt.mama.api.model;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import za.foundation.praekelt.mama.api.db.AppDatabase;

/**
 * Localization model corresponding to unicore.content.model.Localization
 * Due to limitation of kapt, DBFlow table classes need to be
 * created in Java instead of kotlin
 */
@Table(databaseName = AppDatabase.NAME)
public class Localisation extends BaseModel {
    @Column
    @PrimaryKey
    String uuid;

    @Column @Unique String locale;
    @Column @Nullable String image;
    @Column String imageHost;
}
