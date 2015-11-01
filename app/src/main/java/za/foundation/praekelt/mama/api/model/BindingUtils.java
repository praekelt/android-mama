package za.foundation.praekelt.mama.api.model;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import org.jetbrains.anko.DimensionsKt;

import java.util.Iterator;

import kotlin.Sequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import za.foundation.praekelt.mama.R;
import za.foundation.praekelt.mama.app.CategoryListAdapter;
import za.foundation.praekelt.mama.app.CategoryPageAdapter;
import za.foundation.praekelt.mama.util.OrderBy;
import za.foundation.praekelt.mama.util.SharedPrefsUtil;

/**
 * Binding utils class for data binding library
 * Custom binds for layouts are located here
 */
public class BindingUtils {
    @BindingAdapter({"bind:viewPager", "bind:fm", "bind:category_items"})
    public static void setCategoryViewPager(TabLayout tl, ViewPager vp, FragmentManager fm, ObservableArrayList<Category> items) {
        if (vp.getAdapter() == null) {
            System.out.println("no vp adapter present");
            vp.setAdapter(new CategoryPageAdapter(fm,
                    SharedPrefsUtil.INSTANCE$.getLocale(tl.getContext()), OrderBy.POSITION, items));
        } else {
            System.out.println("refreshing vp adapter");
            ((CategoryPageAdapter) vp.getAdapter()).setCategories(items);
        }

        tl.setupWithViewPager(vp);
    }

    @BindingAdapter({"bind:pages"})
    public static void setPages(RecyclerView rv, ObservableArrayList<Page> pages) {
        if (rv.getAdapter() == null) {
            System.out.println("no rv adapter present");
            rv.setAdapter(new CategoryListAdapter(OrderBy.POSITION, pages));
        } else {
            System.out.println("refreshing vp adapter");
            ((CategoryListAdapter) rv.getAdapter()).setPages(pages);
        }
    }

    @BindingAdapter({"android:text", "bind:markdown_format"})
    public static void formatMarkdownTextView(TextView view, String text, boolean format) {
        System.out.println("binding text to text view => "+text.length());
        SpannableStringBuilder formattedPage = new SpannableStringBuilder();
        int start;
        Regex listItemRegex = new Regex("^\\d+\\.\\s");
        Regex boldRegex = new Regex("\\*\\*");
        String [] lines = text.split("\\r\\n");
        //TODO Use object pool for spans and results
        StyleSpan styleSpan;
        int count = 0;
        int changesThisLoop = 0;
        int boldStart = -1;
        int boldEnd = -1;
        MatchResult boldMatch;
        for(String str : lines){
            if(count == 0 || (str.isEmpty() && count > 0)) {
                ++count;
                continue;
            }else
                count = -1;

            Sequence<kotlin.text.MatchResult> boldMatchResultSequence = boldRegex.matchAll(str, 0);
            Iterator<MatchResult> boldIterator = boldMatchResultSequence.iterator();
            while(boldIterator.hasNext()){
                boldMatch = boldIterator.next();
                if(boldStart == -1) {
                    boldStart = formattedPage.length()+boldMatch.getRange().getStart();
                    System.out.println("found bold beginning => "+boldMatch.getRange().getStart());
                    str = str.replaceFirst("\\*\\*", "");
                    ++changesThisLoop;
                }else {
                    boldEnd = formattedPage.length()+boldMatch.getRange().getStart();
                    System.out.println("found bold beginning =>"+formattedPage.length()+boldMatch.getRange().getStart());
                    str = str.replaceFirst("\\*\\*", "");
                    ++changesThisLoop;
                }
            }

            start = formattedPage.length();
            formattedPage.append(str + "\n");
            Sequence<kotlin.text.MatchResult> matchResultSequence = listItemRegex.matchAll(str, 0);
            Iterator<MatchResult> resultIterator = matchResultSequence.iterator();
            while(resultIterator.hasNext()){
                MatchResult result = resultIterator.next();
                System.out.println("found match => " + result.getRange());
                LeadingMarginSpan.Standard lms = new LeadingMarginSpan.Standard(
                        DimensionsKt.dimen(view.getContext(), R.dimen.detail_page_leading_margin));
                styleSpan = new StyleSpan(Typeface.BOLD);
                formattedPage.setSpan(lms, start, start + str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                formattedPage.setSpan(styleSpan, start, start+2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                formattedPage.append("\n");
            }

            if(boldStart > 0 && boldEnd > 0){
                styleSpan = new StyleSpan(Typeface.BOLD);
                if(changesThisLoop == 1)
                    formattedPage.setSpan(styleSpan, boldStart, boldEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                else
                    formattedPage.setSpan(styleSpan, boldStart, boldEnd-2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                boldStart = boldEnd = -1;
            }
        }
        view.setText(formattedPage, TextView.BufferType.SPANNABLE);
    }
}
