package za.foundation.praekelt.mama.api.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import za.foundation.praekelt.mama.api.db.AppDatabase;

/**
 * Category model corresponding to unicore.content.model.Category
 * Due to limitation of kapt, DBFlow table classes need to be
 * created in Java instead of kotlin
 */
@Table(databaseName = AppDatabase.NAME)
public class Category extends BaseModel {
    @NotNull
    @Column
    @PrimaryKey
    String uuid;

    @Column @Nullable String subtitle;
    @Column @NotNull String title;
    @Column int position;
    @Column boolean featuredInNavbar;
    @Column @NotNull String slug;
    @Column @Nullable String image;
    @Column @NotNull String imageHost;
    @Column @Nullable String sourceId;
    @Column @NotNull String localeId;

    @NotNull
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "localeId",
                    columnType = String.class,
                    foreignColumnName = "locale")},
            saveForeignKeyModel = false)
    Localisation language;

    @Nullable
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "sourceId",
                    columnType = String.class,
                    foreignColumnName = "uuid")},
            saveForeignKeyModel = false)
    Category source;

    public Category() {
        uuid = ""; title = ""; subtitle = ""; slug = ""; imageHost = "";
        language = new Localisation(); position = -1; localeId = "";
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull String uuid) {
        this.uuid = uuid;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFeaturedInNavbar() {
        return featuredInNavbar;
    }

    public void setFeaturedInNavbar(boolean featuredInNavbar) {
        this.featuredInNavbar = featuredInNavbar;
    }

    @NotNull
    public String getSlug() {
        return slug;
    }

    public void setSlug(@NotNull String slug) {
        this.slug = slug;
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

    @NotNull
    public Localisation getLanguage() {
        return language;
    }

    public void setLanguage(@NotNull Localisation language) {
        this.language = language;
    }

    @Nullable
    public Category getSource() {
        return source;
    }

    public void setSource(@Nullable Category source) {
        this.source = source;
    }

    @Nullable
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(@Nullable String sourceId) {
        this.sourceId = sourceId;
    }

    @NotNull
    public String getLocaleId() {
        return localeId;
    }

    public void setLocaleId(@NotNull String localeId) {
        this.localeId = localeId;
    }

    //Static references for json field names
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";
    public static final String FIELD_SLUG = "slug";
    public static final String FIELD_FEATURE_IN_NAVBAR = "featured_in_navbar";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_IMAGE_HOST = "image_host";
    public static final String FIELD_SOURCE_ID = "source";
    public static final String FIELD_LOCALE_ID = "language";
}
