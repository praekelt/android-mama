package za.foundation.praekelt.mama.api.model;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import za.foundation.praekelt.mama.api.db.AppDatabase;

/**
 * Category model corresponding to unicore.content.model.Category
 * Due to limitation of kapt, DBFlow table classes need to be
 * created in Java instead of kotlin
 */
@Table(databaseName = AppDatabase.NAME)
public class Category extends BaseModel {
    @Column
    @PrimaryKey
    String uuid;

    @Column @Nullable String subtitle;
    @Column String title;
    @Column int position;
    @Column boolean featuredInNavBar;
    @Column String slug;
    @Column @Nullable String image;
    @Column String imageHost;

    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "languageLocale",
                    columnType = String.class,
                    foreignColumnName = "locale")},
            saveForeignKeyModel = false)
    Localisation language;

    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "source",
                    columnType = String.class,
                    foreignColumnName = "uuid")},
            saveForeignKeyModel = false)
    Category source;
}
