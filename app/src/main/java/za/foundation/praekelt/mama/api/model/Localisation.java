package za.foundation.praekelt.mama.api.model;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    String uuid;

    @Column @Unique @NotNull String locale;
    @Column @Nullable String image;
    @Column @NotNull String imageHost;

    public Localisation() { uuid = ""; locale = ""; imageHost = ""; }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull String uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public String getLocale() {
        return locale;
    }

    public void setLocale(@NotNull String locale) {
        this.locale = locale;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    @NotNull
    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(@NotNull String imageHost) {
        this.imageHost = imageHost;
    }

    //Static references for json field names
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_LOCALE = "locale";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_IMAGE_HOST = "image_host";
}
