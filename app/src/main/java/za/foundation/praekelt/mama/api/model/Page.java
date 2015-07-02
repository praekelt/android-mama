package za.foundation.praekelt.mama.api.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import za.foundation.praekelt.mama.api.db.AppDatabase;
import za.foundation.praekelt.mama.api.db.util.DBStringList;

/**
 * Page model corresponding to unicore.content.model.Page
 * Due to limitation of kapt, DBFlow table classes need to be
 * created in Java instead of kotlin
 */
@Table(databaseName = AppDatabase.NAME)
public class Page extends BaseModel{
    @NotNull
    @Column
    @PrimaryKey
    String uuid;

    @Column @NotNull String title;
    @Column @Nullable String subtitle;
    @Column @NotNull String description;
    @Column @NotNull String slug;
    @Column @NotNull Calendar createdAt;
    @Column @NotNull Calendar modifiedAt;
    @Column @NotNull String content;
    @Column boolean published;
    @Column boolean featured;
    @Column boolean featuredInCategory;
    @Column int position;
    @Column @Nullable String image;
    @Column @NotNull String imageHost;
    @Column @NotNull String localeId;
    @Column @Nullable String sourceId;
    @Column @Nullable String primaryCategoryId;
    @Column @NotNull DBStringList authorTags;
    @Column @NotNull DBStringList linkedPagesIDs;
    @NotNull List<Page> linkedPages;

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
    Page source;

    @NotNull
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "categorySourceId",
                    columnType = String.class,
                    foreignColumnName = "uuid")},
            saveForeignKeyModel = false)
    Category primaryCategory;

    public Page() {
        uuid= ""; title = ""; description = ""; slug = ""; content = ""; imageHost = "";
        createdAt = new GregorianCalendar(); modifiedAt = new GregorianCalendar();
        authorTags = new DBStringList(); linkedPagesIDs = new DBStringList(2);
        linkedPages = new ArrayList<>(); language = new Localisation();
        primaryCategory = new Category(); localeId = ""; sourceId = ""; primaryCategoryId = "";
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull String uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    @NotNull
    public String getSlug() {
        return slug;
    }

    public void setSlug(@NotNull String slug) {
        this.slug = slug;
    }

    @NotNull
    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull Calendar createdAt) {
        this.createdAt = createdAt;
    }

    @NotNull
    public Calendar getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(@NotNull Calendar modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isFeaturedInCategory() {
        return featuredInCategory;
    }

    public void setFeaturedInCategory(boolean featuredInCategory) {
        this.featuredInCategory = featuredInCategory;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
    public DBStringList getAuthorTags() {
        return authorTags;
    }

    public void setAuthorTags(@NotNull DBStringList authorTags) {
        this.authorTags = authorTags;
    }

    @NotNull
    public DBStringList getLinkedPagesIDs() {
        return linkedPagesIDs;
    }

    public void setLinkedPagesIDs(@NotNull DBStringList linkedPagesIDs) {
        this.linkedPagesIDs = linkedPagesIDs;
    }

    @NotNull
    public List<Page> getLinkedPages() {
        return linkedPages;
    }

    public void setLinkedPages(@NotNull List<Page> linkedPages) {
        this.linkedPages = linkedPages;
    }

    @NotNull
    public Localisation getLanguage() {
        return language;
    }

    public void setLanguage(@NotNull Localisation language) {
        this.language = language;
    }

    @Nullable
    public Page getSource() {
        return source;
    }

    public void setSource(@Nullable Page source) {
        this.source = source;
    }

    @NotNull
    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(@NotNull Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    @NotNull
    public String getLocaleId() {
        return localeId;
    }

    public void setLocaleId(@NotNull String localeId) {
        this.localeId = localeId;
    }

    @Nullable
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(@Nullable String sourceId) {
        this.sourceId = sourceId;
    }

    @Nullable
    public String getPrimaryCategoryId() {
        return primaryCategoryId;
    }

    public void setPrimaryCategoryId(@Nullable String primaryCategoryId) {
        this.primaryCategoryId = primaryCategoryId;
    }

    //Static references for json field names
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SLUG = "slug";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_MODIFIED_AT = "modified_at";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_PUBLISHED = "published";
    public static final String FIELD_FEATURED = "featured";
    public static final String FIELD_FEATURE_IN_CATEGORY = "featured_in_category";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_IMAGE_HOST = "image_host";
    public static final String FIELD_LOCALE_ID = "language";
    public static final String FIELD_SOURCE_ID = "source";
    public static final String FIELD_PRIME_CAT_ID = "primary_category";
    public static final String FIELD_AUTHOR_TAGS = "author_tags";
    public static final String FIELD_LINKED_PAGES = "linked_pages";
}
