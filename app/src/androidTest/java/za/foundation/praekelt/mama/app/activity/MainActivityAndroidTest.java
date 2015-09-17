package za.foundation.praekelt.mama.app.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import za.foundation.praekelt.mama.ObservableIdlingResource;
import za.foundation.praekelt.mama.R;
import za.foundation.praekelt.mama.api.model.Category;
import za.foundation.praekelt.mama.util.SharedPrefsUtil;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static za.foundation.praekelt.mama.RecyclerViewMatcher.withRecyclerView;

/**
 * Created by eduardokolomajr on 2015/09/12.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityAndroidTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    ObservableIdlingResource tir;

    @Test
    public void testCategoryTabs() {
        //If if no categories yet check empty list notification is shown
        if (SharedPrefsUtil.INSTANCE$.getCommitFromSharedPrefs(rule.getActivity()).equals(""))
            onView(withId(R.id.tv_empty_list)).check(matches(isDisplayed()));

        tir = new ObservableIdlingResource(rule.getActivity().getViewModel().getRepoObs());
        registerIdlingResources(tir);

        //check each category is shown
        Assertions.assertThat(rule.getActivity().getViewModel().getCategories()).isNotEmpty();
        for (Category category : rule.getActivity().getViewModel().getCategories()) {
            onView(withId(R.id.tabs)).check(matches(hasDescendant(withText(category.getTitle()))));

            //swipe to next category, ensures that if more categories than screen
            // width title will be visible during test
            onView(withId(R.id.viewpager)).perform(swipeLeft());
        }
        unregisterIdlingResources(tir);
    }

    @Test
    public void testStoriesLoad() {
        tir = new ObservableIdlingResource(rule.getActivity().getViewModel().getRepoObs());
        registerIdlingResources(tir);

        Assertions.assertThat(rule.getActivity().getViewModel().getCategories()).isNotEmpty();
        //Ensure that at least all the categories have at least one non-empty item
        for (int i = 0; i < rule.getActivity().getViewModel().getCategories().size(); i++) {
            //Ensure that the title is not empty
            onView(withRecyclerView(R.id.rv_category_list).atPosition(0))
                    .check(matches(hasDescendant(allOf(
                            withId(R.id.tv_title_category_list_item),
                            withText(not(""))))));

            //swipe to next category
            onView(withId(R.id.viewpager)).perform(swipeLeft());
        }
        unregisterIdlingResources(tir);
    }
}
