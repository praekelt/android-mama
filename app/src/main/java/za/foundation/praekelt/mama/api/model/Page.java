package za.foundation.praekelt.mama.api.model;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;
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
    @Column
    @PrimaryKey
    String uuid;

    @Column @Nullable String subtitle;
    @Column String description;
    @Column String title;
    @Column Calendar createdAt;
    @Column Calendar modifiedAt;
    @Column boolean published;
    @Column String slug;
    @Column String content;
    @Column boolean featured;
    @Column boolean featuredInCategory;
    @Column int position;
    @Column @Nullable String image;
    @Column String imageHost;
    @Column String authorTags;
    @Column
    DBStringList linkedPagesIDs;
    List<Page> linkedPages;

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
    Page source;

    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "source",
                    columnType = String.class,
                    foreignColumnName = "uuid")},
            saveForeignKeyModel = false)
    Category primaryCategory;

    @OneToMany(methods = {OneToMany.Method.ALL})
    public List<Page> getLinkedPages() {
        if(linkedPages == null) {
            linkedPages = new Select()
                    .from(Page.class)
                    .where(Condition.column(Page$Table.UUID).in(linkedPagesIDs))
                    .queryList();
        }
        return linkedPages;
    }
}
