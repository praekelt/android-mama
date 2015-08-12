package za.foundation.praekelt.mama.api.rest.adapter

import android.util.Log
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import za.foundation.praekelt.mama.api.db.util.DBStringList
import za.foundation.praekelt.mama.api.model.Category
import za.foundation.praekelt.mama.api.model.Localisation
import za.foundation.praekelt.mama.api.model.Page
import za.foundation.praekelt.mama.api.rest.model.FormattedDiff
import za.foundation.praekelt.mama.api.rest.model.FormattedDiff.DiffType
import za.foundation.praekelt.mama.util.Constants
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Util funtions that processes different classes that REST service retrieves
 */

fun processAuthors(input: JsonReader): DBStringList {
    var authors: DBStringList = DBStringList()
    input.beginArray()
    while (input.hasNext()) {
        authors.add(input.nextString())
    }
    input.endArray()
    return authors
}

fun processCategories(input: JsonReader): MutableList<Category> {
    var categories: MutableList<Category> = ArrayList<Category>()
    input.beginArray()
    while (input.hasNext()) {
        var category: Category = Category()
        input.beginObject()
        while (input.hasNext()) {
            val nextName = input.nextName()
            when (nextName) {
                Category.FIELD_UUID -> category.setUuid(input.nextString())
                Category.FIELD_IMAGE -> category.setImage(processImage(input))
                Category.FIELD_IMAGE_HOST -> category.setImageHost(input.nextString())
                Category.FIELD_POSITION -> category.setPosition(input.nextInt())
                Category.FIELD_SLUG -> category.setSlug(input.nextString())
                Category.FIELD_TITLE -> category.setTitle(input.nextString())
                Category.FIELD_SUBTITLE -> category.setSubtitle(input.nextString())
                Category.FIELD_FEATURE_IN_NAVBAR -> category.setFeaturedInNavbar(input.nextBoolean())
                Category.FIELD_SOURCE_ID -> category.setSourceId(processString(input));
                Category.FIELD_LOCALE_ID -> category.setLocaleId(input.nextString());
                else -> {
                    Log.d("AdapterUtils", "procCat unknown tag found => $nextName")
                    input.skipValue()
                }
            }
        }
        input.endObject()
        categories.add(category)
    }
    input.endArray()
    return categories
}

fun processDate(dateString: String): Calendar {
    var date: Calendar = GregorianCalendar()
    date.setTime(SimpleDateFormat(Constants.REMOTE_DATE_FORMAT).parse(dateString.substring(0, dateString.indexOf('T'))))
    return date
}

fun processDiffs(input: JsonReader): List<FormattedDiff> {
    var diffs: MutableList<FormattedDiff> = ArrayList<FormattedDiff>()

    input.beginArray()
    while (input.hasNext()) {
        var diff: FormattedDiff = FormattedDiff()
        input.beginObject()
        while (input.hasNext()) {
            val nextName = input.nextName()
            when (nextName) {
                FormattedDiff.FIELD_PATH -> diff.path = input.nextString()
                FormattedDiff.FIELD_TYPE -> {
                    when (input.nextString()) {
                        "A" -> diff.type = DiffType.ADDED
                        "D" -> diff.type = DiffType.DELETED
                        "M" -> diff.type = DiffType.MODIFIED
                        "R" -> diff.type = DiffType.RENAMED
                        else -> diff.type = DiffType.NOT_SET
                    }
                }
                else -> {
                    Log.d("AdapterUtils", "procDiffs found unknown tag => $nextName")
                    input.skipValue()
                }
            }
        }
        input.endObject()
        diffs.add(diff)
    }
    input.endArray()
    return diffs
}

fun processImage(input: JsonReader) :String? {
    val nextToken: JsonToken = input.peek()
    if(nextToken != JsonToken.NULL)
        return input.nextString()
    else
        input.skipValue()
        return null
}

fun processLinks(input: JsonReader): DBStringList {
    var links: DBStringList = DBStringList()
    input.beginArray()
    while (input.hasNext()) {
        links.add(input.nextString())
    }
    input.endArray()
    return links
}

fun processLocales(input: JsonReader): List<Localisation> {
    var locales: MutableList<Localisation> = ArrayList<Localisation>()
    input.beginArray()
    while (input.hasNext()) {
        var locale: Localisation = Localisation()
        input.beginObject()
        while (input.hasNext()) {
            val nextName = input.nextName()
            when (nextName) {
                Localisation.FIELD_UUID -> locale.setUuid(input.nextString())
                Localisation.FIELD_IMAGE -> locale.setImage(processImage(input))
                Localisation.FIELD_IMAGE_HOST -> locale.setImageHost(input.nextString())
                Localisation.FIELD_LOCALE -> locale.setLocale(input.nextString())
                else -> {
                    Log.d("AdapterUtils", "procLocales found unknown tag => $nextName")
                    input.skipValue()
                }
            }
        }
        input.endObject()
        locales.add(locale)
    }
    input.endArray()
    return locales
}

fun processPages(input: JsonReader): List<Page> {
    var pages: MutableList<Page> = ArrayList<Page>()
    input.beginArray()
    while (input.hasNext()) {
        var page: Page = Page()
        input.beginObject()
        while (input.hasNext()) {
            val nextName:String = input.nextName()
            when (nextName) {
                Page.FIELD_UUID -> page.setUuid(input.nextString())
                Page.FIELD_TITLE -> page.setTitle(input.nextString())
                Page.FIELD_SUBTITLE -> page.setSubtitle(input.nextString())
                Page.FIELD_DESCRIPTION -> page.setDescription(input.nextString())
                Page.FIELD_SLUG -> page.setSlug(input.nextString())
                Page.FIELD_CONTENT -> page.setContent(input.nextString())
                Page.FIELD_IMAGE_HOST -> page.setImageHost(input.nextString())
                Page.FIELD_IMAGE -> page.setImage(processImage(input))
                Page.FIELD_PUBLISHED -> page.setPublished(processPublished(input))
                Page.FIELD_FEATURED -> page.setFeatured(input.nextBoolean())
                Page.FIELD_FEATURE_IN_CATEGORY -> page.setFeaturedInCategory(input.nextBoolean())
                Page.FIELD_SOURCE_ID -> page.setSourceId(processString(input))
                Page.FIELD_LOCALE_ID -> page.setLocaleId(input.nextString())
                Page.FIELD_PRIME_CAT_ID -> page.setPrimaryCategoryId(processString(input))
                Page.FIELD_POSITION -> page.setPosition(input.nextInt())
                Page.FIELD_CREATED_AT -> page.setCreatedAt(processDate(input.nextString()))
                Page.FIELD_MODIFIED_AT -> page.setModifiedAt(processDate(input.nextString()))
                Page.FIELD_AUTHOR_TAGS -> page.setAuthorTags(processAuthors(input))
                Page.FIELD_LINKED_PAGES -> page.setLinkedPagesIDs(processLinks(input))
                else -> {
                    Log.d("AdapterUtils", "procPages found unknown tag => $nextName")
                    input.skipValue()
                }
            }
        }
        input.endObject()
        pages.add(page)
    }
    input.endArray()
    return pages
}

fun processPublished(input: JsonReader): Boolean{
    val nextToken: JsonToken = input.peek()
    if(nextToken != JsonToken.NULL)
        return input.nextBoolean()
    else
        input.skipValue()
        return false
}

fun processString(input: JsonReader): String?{
    val nextToken: JsonToken = input.peek()
    if(nextToken != JsonToken.NULL)
        return input.nextString()
    else
        input.skipValue()
        return null
}