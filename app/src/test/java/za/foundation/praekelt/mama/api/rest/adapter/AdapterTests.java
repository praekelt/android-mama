package za.foundation.praekelt.mama.api.rest.adapter;

import android.content.Context;
import android.os.Build;

import com.google.gson.stream.JsonReader;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import za.foundation.praekelt.mama.BuildConfig;
import za.foundation.praekelt.mama.api.db.AppDatabase;
import za.foundation.praekelt.mama.api.model.Category;
import za.foundation.praekelt.mama.api.model.Localisation;
import za.foundation.praekelt.mama.api.model.Page;
import za.foundation.praekelt.mama.api.rest.model.FormattedDiff;
import za.foundation.praekelt.mama.api.rest.model.Repo;
import za.foundation.praekelt.mama.api.rest.model.RepoDiff;
import za.foundation.praekelt.mama.api.rest.model.RepoPull;
import za.foundation.praekelt.mama.api.rest.model.RepoStatus;
import za.foundation.praekelt.mama.app.App;
import za.foundation.praekelt.mama.util.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for
 * {@link RepoStatusAdapter}
 * {@link RepoDiffAdapter}
 * {@link RepoPullAdapter}
 * {@link RepoAdapter}
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, application = App.class)
public class AdapterTests{
    Context context;

    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
        FlowManager.init(context);
        FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase().acquireReference();
    }

    @After
    public void tearDown() {
        FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase().releaseReference();
        FlowManager.getDatabase(AppDatabase.NAME).reset(context);
        FlowManager.destroy();
    }

    @Test
    public void testRepoStatusAdapter() throws IOException {
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testStatus.json"));
        RepoStatusAdapter statusAdapter = new RepoStatusAdapter();
        RepoStatus status = statusAdapter.read(jr);
        long currentTime = System.currentTimeMillis();

        assertEquals("repo name", Constants.REPO_NAME, status.getName());
        assertNotEquals("commit index", "", status.getCommit());
        assertTrue("timestamp", status.getTimestamp().getTimeInMillis() < currentTime);
    }

    @Test
    public void testRepoDiffAdapter() throws IOException{
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testDiff.json"));
        RepoDiffAdapter diffAdapter = new RepoDiffAdapter();
        RepoDiff repoDiff = diffAdapter.read(jr);
        String commitId = "0ade8cf2f086ee955050ca4ddbfcb887454d683e";

        assertEquals("repo name", Constants.REPO_NAME, repoDiff.getName());
        assertEquals("request commit id", commitId, repoDiff.getPreviousIndex());
        assertNotEquals("response commit it", "", repoDiff.getCurrentIndex());
        assertTrue("diffs length", repoDiff.getDiffs().size() > 0);
        for (FormattedDiff diff : repoDiff.getDiffs()) {
            assertNotEquals("diff path", "", diff.getPath());
            assertNotEquals("diff type", FormattedDiff.DiffType.NOT_SET, diff.getType());
        }
    }

    @Test
    public void testRepoAdapter() throws FileNotFoundException {
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testRepo.json"));
        RepoAdapter repoAdapter = new RepoAdapter();
        Repo repo = repoAdapter.read(jr);

        assertNotEquals("commit index", repo.getCommit(), "");
        assertTrue("categories length", repo.getCategories().size() > 0);
        assertTrue("locales length", repo.getLocales().size() > 0);
        assertTrue("pages length", repo.getPages().size() > 0);

        for (Localisation locale : repo.getLocales()) {
            assertNotEquals("uuid", "", locale.getUuid());
            assertNotEquals("locale", "", locale.getLocale());
            assertNotEquals("image host", "", locale.getImageHost());
        }

        for (Category category : repo.getCategories()) {
            assertNotEquals("uuid", "", category.getUuid());
            assertNotEquals("image host", "", category.getImageHost());
            assertNotEquals("title", "", category.getTitle());
            assertNotEquals("position", -1, category.getPosition());
            assertNotEquals("slug", "", category.getSource());
        }

        for (Page page : repo.getPages()) {
            assertNotEquals("uuid", "", page.getUuid());
            assertNotEquals("image host", "", page.getImageHost());
            assertNotEquals("title", "", page.getTitle());
            assertNotEquals("position", -1, page.getPosition());
            assertNotEquals("slug", "", page.getSource());
//            assertNotEquals("desc", "", page.getDescription());
            assertNotEquals("content", "", page.getContent());
        }
    }

    @Test
    public void testRepoPullAdapter() throws FileNotFoundException {
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testPull.json"));
        RepoPullAdapter pullAdapter = new RepoPullAdapter();
        RepoPull repoPull = pullAdapter.read(jr);
        assertNotEquals("commit id", "", repoPull.getCommit());
        assertTrue("categories length", repoPull.getCategories().size() > 0);
        assertTrue("locales length", repoPull.getLocales().size() > 0);
        assertTrue("pages length", repoPull.getPages().size() > 0);
        assertTrue("diff length", repoPull.getDiffs().size() > 0);

        checkCategories(repoPull.getCategories());
        checkDiffs(repoPull.getDiffs());
        checkLocales(repoPull.getLocales());
        checkPages(repoPull.getPages());
    }

    public void checkCategories(List<Category> categories){
        for (Category category : categories) {
            assertNotEquals("uuid", "", category.getUuid());
            assertNotEquals("image host", "", category.getImageHost());
            assertNotEquals("title", "", category.getTitle());
            assertNotEquals("position", -1, category.getPosition());
            assertNotEquals("slug", "", category.getSource());
        }
    }

    public void checkDiffs(List<FormattedDiff> diffs){
        for (FormattedDiff diff : diffs) {
            assertNotEquals("diff path", "", diff.getPath());
            assertNotEquals("diff type", FormattedDiff.DiffType.NOT_SET, diff.getType());
        }
    }

    public void checkLocales(List<Localisation> locales){
        for (Localisation locale : locales) {
            assertNotEquals("uuid", "", locale.getUuid());
            assertNotEquals("locale", "", locale.getLocale());
            assertNotEquals("image host", "", locale.getImageHost());
        }
    }

    public void checkPages(List<Page> pages){
        for (Page page : pages) {
            assertNotEquals("uuid", "", page.getUuid());
            assertNotEquals("image host", "", page.getImageHost());
            assertNotEquals("title", "", page.getTitle());
            assertNotEquals("position", -1, page.getPosition());
            assertNotEquals("slug", "", page.getSource());
            assertNotEquals("content", "", page.getContent());
            assertNotEquals("locale id", "", page.getLocaleId());
        }
    }
}
