package za.foundation.praekelt.mama.app.viewmodel;

import android.content.Context;
import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.util.ActivityController;

import java.lang.reflect.Field;

import za.foundation.praekelt.mama.BuildConfig;
import za.foundation.praekelt.mama.R;
import za.foundation.praekelt.mama.api.db.AppDatabase;
import za.foundation.praekelt.mama.api.rest.RestPackage;
import za.foundation.praekelt.mama.app.App;
import za.foundation.praekelt.mama.app.activity.MainActivity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link MainActivityViewModel}
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = App.class)
public class MainActivityViewModelTest {
    ActivityController<MainActivity> activityController;
    MainActivityViewModel viewModel;
    Context context;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(MainActivity.class).create();
        context = ShadowApplication.getInstance().getApplicationContext();
        RestPackage.setLoadTestService(true);
        FlowManager.init(context);
        FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase().acquireReference();
    }

    @After
    public void tearDown() {
        FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase().releaseReference();
        FlowManager.getDatabase(AppDatabase.NAME).reset(context);
        FlowManager.destroy();
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
    public void testInit() {
        viewModel = new MainActivityViewModel(activityController.get());
        assertThat(viewModel.getApp()).isNotNull();
        assertThat(viewModel.getApp()).isEqualTo(activityController.get().getApplication());
        assertThat(viewModel.getRepoObs()).isNotNull();
    }

    @Test
    public void testOnAttach() {
        viewModel = new MainActivityViewModel(activityController.get());
        viewModel.onAttachActivity(activityController.get());
        assertThat(viewModel.getFm()).isNotNull();
        assertThat(viewModel.getFm()).isEqualTo(activityController.get().getSupportFragmentManager());
        assertThat(viewModel.getVp()).isNotNull();
        assertThat(viewModel.getVp()).isEqualTo(activityController.get().findViewById(R.id.viewpager));
    }

    @Test
    public void testOnDestroy() {
        viewModel = new MainActivityViewModel(activityController.get());
        viewModel.onAttachActivity(activityController.get());
        viewModel.onDestroy();
        assertThat(viewModel.getAct()).isNull();
        assertThat(viewModel.getFm()).isNull();
        assertThat(viewModel.getVp()).isNull();
    }
}
