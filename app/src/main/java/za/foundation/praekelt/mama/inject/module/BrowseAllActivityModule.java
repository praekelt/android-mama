package za.foundation.praekelt.mama.inject.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import dagger.Module;
import dagger.Provides;
import za.foundation.praekelt.mama.app.activity.BrowseAllActivity;
import za.foundation.praekelt.mama.app.viewmodel.BaseViewModel;
import za.foundation.praekelt.mama.app.viewmodel.BrowseAllActivityViewModel;
import za.foundation.praekelt.mama.inject.scope.ActivityScope;

/**
 * Module for the browse all activity
 */
@Module
public class BrowseAllActivityModule {
    private BrowseAllActivity activity;

    public BrowseAllActivityModule(BrowseAllActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public BrowseAllActivityViewModel provideViewModel(){
        BaseViewModel cached = activity.appComponent().app()
                .getCachedViewModel(BrowseAllActivityViewModel.TAG);
        if (cached != null)
            return (BrowseAllActivityViewModel) cached;
        else
            return new BrowseAllActivityViewModel(activity);
    }

    @Provides
    @ActivityScope
    public RecyclerView.LayoutManager provideLayoutManager(){
        return new LinearLayoutManager(activity);
    }
}
