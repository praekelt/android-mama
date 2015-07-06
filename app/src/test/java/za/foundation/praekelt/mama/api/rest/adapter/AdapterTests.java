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

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(status.getName()).isEqualTo(Constants.REPO_NAME);
        assertThat(status.getCommit()).isNotEqualTo("");
        assertThat(status.getTimestamp().getTimeInMillis()).isLessThan(currentTime);
    }

    @Test
    public void testRepoDiffAdapter() throws IOException{
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testDiff.json"));
        RepoDiffAdapter diffAdapter = new RepoDiffAdapter();
        RepoDiff repoDiff = diffAdapter.read(jr);
        String commitId = "0ade8cf2f086ee955050ca4ddbfcb887454d683e";

        assertThat(repoDiff.getName()).isEqualTo(Constants.REPO_NAME);
        assertThat(repoDiff.getPreviousIndex()).isEqualTo(commitId);
        assertThat(repoDiff.getCurrentIndex()).isNotEqualTo("");
        assertThat(repoDiff.getDiffs()).isNotEmpty();
        checkDiffs(repoDiff.getDiffs());
    }

    @Test
    public void testRepoAdapter() throws FileNotFoundException {
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testRepo.json"));
        RepoAdapter repoAdapter = new RepoAdapter();
        Repo repo = repoAdapter.read(jr);

        assertThat(repo.getCommit()).isNotEqualTo("");
        assertThat(repo.getCategories()).isNotEmpty();
        assertThat(repo.getLocales()).isNotEmpty();
        assertThat(repo.getPages()).isNotEmpty();
        checkCategories(repo.getCategories());
        checkLocales(repo.getLocales());
        checkPages(repo.getPages());
    }

    @Test
    public void testRepoPullAdapter() throws FileNotFoundException {
        JsonReader jr = new JsonReader(new FileReader("src/test/testFiles/testPull.json"));
        RepoPullAdapter pullAdapter = new RepoPullAdapter();
        RepoPull repoPull = pullAdapter.read(jr);

        assertThat(repoPull.getCommit()).isNotEqualTo("");
        assertThat(repoPull.getCategories()).isNotEmpty();
        assertThat(repoPull.getLocales()).isNotEmpty();
        assertThat(repoPull.getPages()).isNotEmpty();
        assertThat(repoPull.getDiffs()).isNotEmpty();
        checkCategories(repoPull.getCategories());
        checkDiffs(repoPull.getDiffs());
        checkLocales(repoPull.getLocales());
        checkPages(repoPull.getPages());
    }

    public void checkCategories(List<Category> categories){
        for (Category category : categories) {
            assertThat(category.getUuid()).isNotEqualTo("");
            assertThat(category.getImageHost()).isNotEqualTo("");
            assertThat(category.getTitle()).isNotEqualTo("");
            assertThat(category.getPosition()).isNotNegative();
        }
    }

    public void checkDiffs(List<FormattedDiff> diffs){
        for (FormattedDiff diff : diffs) {
            assertThat(diff.getPath()).isNotEqualTo("");
            assertThat(diff.getType()).isNotEqualTo(FormattedDiff.DiffType.NOT_SET);
        }
    }

    public void checkLocales(List<Localisation> locales){
        for (Localisation locale : locales) {
            assertThat(locale.getUuid()).isNotEqualTo("");
            assertThat(locale.getLocale()).isNotEqualTo("");
            assertThat(locale.getImageHost()).isNotEqualTo("");
        }
    }

    public void checkPages(List<Page> pages){
        for (Page page : pages) {
            assertThat(page.getUuid()).isNotEqualTo("");
            assertThat(page.getImageHost()).isNotEqualTo("");
            assertThat(page.getTitle()).isNotEqualTo("");
            assertThat(page.getPosition()).isNotNegative();
            assertThat(page.getLocaleId()).isNotEqualTo("");
            assertThat(page.getContent()).isNotEqualTo("");
        }
    }
}
