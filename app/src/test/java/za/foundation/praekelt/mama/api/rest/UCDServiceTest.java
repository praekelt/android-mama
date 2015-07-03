package za.foundation.praekelt.mama.api.rest;

import android.content.Context;
import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import retrofit.RestAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import za.foundation.praekelt.mama.BuildConfig;
import za.foundation.praekelt.mama.api.db.AppDatabase;
import za.foundation.praekelt.mama.app.App;
import za.foundation.praekelt.mama.util.Constants;

import static org.assertj.core.api.Assertions.assertThat;
import static za.foundation.praekelt.mama.api.rest.RestPackage.createUCDService;
import static za.foundation.praekelt.mama.api.rest.RestPackage.createUCDServiceGson;

/**
 * Tests for {@link UCDService}
 * Make sure an instance of unicore.distribute is running
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = App.class)
public class UCDServiceTest {
    UCDService ucdService;
    String commitId = "0ade8cf2f086ee955050ca4ddbfcb887454d683e";
    Context context;

    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
        FlowManager.init(context);
        FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase().acquireReference();

        RestAdapter restAdapter = createUCDService(createUCDServiceGson());

        ucdService = restAdapter.create(UCDService.class);
    }

    @After
    public void tearDown() {
        FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase().releaseReference();
        FlowManager.getDatabase(AppDatabase.NAME).reset(context);
        FlowManager.destroy();
    }

    @Test
    public void testGetRepoStatus() {
        long currentTime = System.currentTimeMillis();

        Observable.just(ucdService.getRepoStatus().toBlocking().single())
                .subscribe(status -> {
                    assertThat(status.getName()).isEqualTo(Constants.REPO_NAME);
                    assertThat(status.getCommit()).isNotEqualTo("");
                    assertThat(status.getTimestamp().getTimeInMillis()).isLessThan(currentTime);
                });
    }

    @Test
    public void testCloneRepo() {
        Observable.just(ucdService.cloneRepo().toBlocking().single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repo -> {
                    assertThat(repo.getCommit()).isNotEqualTo("");
                    assertThat(repo.getCategories().size()).isGreaterThan(0);
                    assertThat(repo.getLocales().size()).isGreaterThan(0);
                    assertThat(repo.getPages().size()).isGreaterThan(0);
                });
    }

    @Test
    public void testDiffRepo() {
        Observable.just(ucdService.getRepoDiff(commitId).toBlocking().single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repoDiff -> {
                    assertThat(repoDiff.getName()).isEqualTo(Constants.REPO_NAME);
                    assertThat(repoDiff.getPreviousIndex()).isEqualTo(commitId);
                    assertThat(repoDiff.getCurrentIndex()).isEqualTo("");
                    assertThat(repoDiff.getDiffs().size()).isGreaterThan(0);
                });
    }

    @Test
    public void testPullRepo() {
        Observable.just(ucdService.pullRepo(commitId).toBlocking().single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repoPull -> {
                    assertThat(repoPull.getCommit()).isNotEqualTo("");
                    assertThat(repoPull.getCategories().size()).isGreaterThan(0);
                    assertThat(repoPull.getLocales().size()).isGreaterThan(0);
                    assertThat(repoPull.getPages().size()).isGreaterThan(0);
                });
    }
}