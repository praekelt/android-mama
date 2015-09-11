package za.foundation.praekelt.mama.app;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import za.foundation.praekelt.mama.BuildConfig;
import za.foundation.praekelt.mama.app.activity.MainActivity;
import za.foundation.praekelt.mama.util.Constants;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MainActivity}
 * Duplicate due to bug while testing. Running multiple tests on activity
 * currently makes one pass and the other fail due to some threading issues.
 * Tests are fine, framework is just a little buggy.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = App.class)
public class MainActivityTest2 {
    ActivityController<MainActivity> activityController;
    @Before
    public void setUp(){
        activityController = Robolectric.buildActivity(MainActivity.class).create();
    }

    @Test
    public void testPullRepo(){
        SharedPreferences defaultSharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(activityController.get());

        String defaultCommit = "!!@@##";
        defaultSharedPreferences.edit().putString(
                Constants.SHARED_PREFS_COMMIT, "5289bd3a514251234638bd01d269fd7b2b0d2665").apply();

        String commit = defaultSharedPreferences
                .getString(Constants.SHARED_PREFS_COMMIT, defaultCommit);

//        activityController.get().setUcdService(RestPackage.createTestUCDService(RestPackage.createUCDServiceGson()));
        assertThat(commit).isEqualTo("5289bd3a514251234638bd01d269fd7b2b0d2665");

        activityController.resume();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        commit = defaultSharedPreferences
                .getString(Constants.SHARED_PREFS_COMMIT, defaultCommit);

        assertThat(commit).isNotEqualTo(defaultCommit);
        assertThat(commit).isNotEqualTo("5289bd3a514251234638bd01d269fd7b2b0d2665");
    }
}
