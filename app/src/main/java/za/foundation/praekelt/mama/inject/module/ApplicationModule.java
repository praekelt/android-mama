package za.foundation.praekelt.mama.inject.module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import za.foundation.praekelt.mama.app.App;

/**
 * Created by eduardokolomajr on 2015/09/10.
 */
@Module
public class ApplicationModule {
    private App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return app;
    }

    @Provides
    @Singleton
    public App provideApplication()

    {
        return app;
    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return app.getBus();
    }
}
