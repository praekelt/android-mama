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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
                    assertEquals("repo name", Constants.REPO_NAME, status.getName());
                    assertNotEquals("commit index", "", status.getCommit());
                    assertTrue("timestamp", status.getTimestamp().getTimeInMillis() < currentTime);
                });
    }

    @Test
    public void testCloneRepo() {
        Observable.just(ucdService.cloneRepo().toBlocking().single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repo -> {
                    assertNotEquals("commit index", repo.getCommit(), "");
                    assertTrue("categories length", repo.getCategories().size() > 0);
                    assertTrue("locales length", repo.getLocales().size() > 0);
                    assertTrue("pages length", repo.getPages().size() > 0);
                });
    }

    @Test
    public void testDiffRepo() {
        Observable.just(ucdService.getRepoDiff(commitId).toBlocking().single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repoDiff -> {
                    assertEquals("repo name", Constants.REPO_NAME, repoDiff.getName());
                    assertEquals("request commit id", commitId, repoDiff.getPreviousIndex());
                    assertNotEquals("response commit it", "", repoDiff.getCurrentIndex());
                    assertTrue("diffs length", repoDiff.getDiffs().size() > 0);
                });
    }

    @Test
    public void testPullRepo() {
        Observable.just(ucdService.pullRepo(commitId).toBlocking().single())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repoPull -> {
                    assertNotEquals("commit id", "", repoPull.getCommit());
                    assertTrue("categories length", repoPull.getCategories().size() > 0);
                    assertTrue("locales length", repoPull.getLocales().size() > 0);
                    assertTrue("pages length", repoPull.getPages().size() > 0);
                });
    }
}