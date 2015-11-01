package za.foundation.praekelt.mama.inject.module;

import android.content.Context;
import android.util.Log;

import dagger.Module;
import dagger.Provides;
import za.foundation.praekelt.mama.app.activity.DetailPageActivity;
import za.foundation.praekelt.mama.app.viewmodel.DetailPageActivityViewModel;
import za.foundation.praekelt.mama.inject.scope.ActivityScope;

/**
 * Created by eduardokolomajr on 2015/09/14.
 */
@Module
public class DetailPageActivityModule {
    public static final String TAG = DetailPageActivityModule.class.getName();
    final private DetailPageActivity activity;

    public DetailPageActivityModule(DetailPageActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public Context provideContext(){
        return activity;
    }

    @Provides
    @ActivityScope
    public String providePageUuid(){
        if(activity.getIntent() != null){
            Log.i(TAG, "intent not null");
            Log.i(TAG, "returning "+activity.getIntent().getStringExtra(DetailPageActivity.argsKeys.uuidKey));
            return activity.getIntent().getStringExtra(DetailPageActivity.argsKeys.uuidKey);
        }else{
            Log.i(TAG, "intent is null");
        }
        return "";
    }

    @Provides
    @ActivityScope
    public DetailPageActivityViewModel provideViewModel(String pageUuid){
        return new DetailPageActivityViewModel(pageUuid);
    }
}
