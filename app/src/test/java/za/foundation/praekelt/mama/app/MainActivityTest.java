package za.foundation.praekelt.mama.app;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.lang.reflect.Field;

import za.foundation.praekelt.mama.BuildConfig;
import za.foundation.praekelt.mama.app.activity.MainActivity;
import za.foundation.praekelt.mama.util.Constants;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link za.foundation.praekelt.mama.app.activity.MainActivity}
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = App.class)
public class MainActivityTest {
    ActivityController<MainActivity> activityController;
    @Before
    public void setUp(){
        activityController = Robolectric.buildActivity(MainActivity.class);
    }

    @After
    public void tearDown(){
        try {
            Field field = FlowManager.class.getDeclaredField("mDatabaseHolder");
            field.setAccessible(true);
            field.set(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCloneRepo(){
        SharedPreferences defaultSharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(activityController.get());

        String defaultCommit = "!!@@##";
        String commit = defaultSharedPreferences
                .getString(Constants.SHARED_PREFS_COMMIT, defaultCommit);

//        activityController.get().setUcdService(RestPackage.createTestUCDService(RestPackage.createUCDServiceGson()));
        assertThat(commit).isEqualTo(defaultCommit);

        activityController.create().resume();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        commit = defaultSharedPreferences
                .getString(Constants.SHARED_PREFS_COMMIT, defaultCommit);

        assertThat(commit).isNotEqualTo(defaultCommit);
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

        activityController.create().resume();

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
