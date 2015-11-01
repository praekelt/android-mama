package za.foundation.praekelt.mama.inject.module;


import android.content.Context;

import org.jetbrains.anko.Sdk15ServicesKt;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import za.foundation.praekelt.mama.app.activity.MainActivity;
import za.foundation.praekelt.mama.app.viewmodel.BaseViewModel;
import za.foundation.praekelt.mama.app.viewmodel.MainActivityViewModel;
import za.foundation.praekelt.mama.inject.scope.ActivityScope;

/**
 * Created by eduardokolomajr on 2015/09/10.
 */
@Module
public class MainActivityModule {
    private MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public Context provideActivityContext(){
        return activity;
    }

    @Provides
    @ActivityScope
    public MainActivityViewModel ProvideViewModel(){
        BaseViewModel cached = activity.getActivityComp().app()
                .getCachedViewModel(MainActivityViewModel.TAG);
        if (cached != null)
            return (MainActivityViewModel) cached;
        else
            return new MainActivityViewModel(activity);
    }

    @Provides
    public Observable<Boolean> provideNetworkObservable(){
        return Observable.just(Sdk15ServicesKt.getConnectivityManager(activity).getActiveNetworkInfo())
                .map(networkInfo -> (networkInfo != null && networkInfo.isConnected()));
    }
}
