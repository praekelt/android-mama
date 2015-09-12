package za.foundation.praekelt.mama.inject.module;

import android.content.Context;
import android.util.Log;

import org.jetbrains.anko.AnkoPackage;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import za.foundation.praekelt.mama.api.db.util.DBTransaction;
import za.foundation.praekelt.mama.api.rest.RestPackage;
import za.foundation.praekelt.mama.api.rest.UCDService;
import za.foundation.praekelt.mama.api.rest.model.Repo;
import za.foundation.praekelt.mama.api.rest.model.RepoPull;
import za.foundation.praekelt.mama.app.viewmodel.MainActivityViewModel;
import za.foundation.praekelt.mama.util.SharedPrefsUtil;

/**
 * Created by eduardokolomajr on 2015/09/10.
 */
@Module
public class MainActivityViewModelModule {
    public static final String TAG = MainActivityViewModelModule.class.getSimpleName();
    private MainActivityViewModel viewModel;

    public MainActivityViewModelModule(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Provides
    public Context provideAppContext(){
        return viewModel.getApp();
    }

    @Provides
    public UCDService provideUCDService(){
        return RestPackage.createUCDService(RestPackage.createUCDServiceGson());
    }

    @Provides
    public Observable<Boolean> provideNetworkObservable(){
        return Observable.just(AnkoPackage.getConnectivityManager(viewModel.getApp()).getActiveNetworkInfo())
                .map(networkInfo -> (networkInfo != null && networkInfo.isConnected()));
    }

    /**
     * Creates an observable used to clone the repo for the first time
     */
    public Observable<Repo> createCloneObs(Observable<Boolean> networkObs, UCDService ucdService,
                                           Context ctx){
        return networkObs.filter(connected -> connected)
                .map(connected -> SharedPrefsUtil.INSTANCE$.getCommitFromSharedPrefs(ctx))
                .filter(commit -> commit.equals(""))
                .doOnNext(commit -> Log.i(TAG, "repo doesn't exist"))
                .flatMap(commit -> ucdService.cloneRepo());
    }

    /**
     * Creates an observable used to pull changes from the repo
     */
    public Observable<RepoPull> createUpdateObs(Observable<Boolean> networkObs, UCDService ucdService,
                                                Context ctx){
        return networkObs.filter(connected -> connected)
                .map(connected -> SharedPrefsUtil.INSTANCE$.getCommitFromSharedPrefs(ctx))
                .filter(commit -> !commit.equals(""))
                .doOnNext(commit -> Log.i(TAG, "repo exists, checking for update"))
                .flatMap(commit -> ucdService.getRepoStatus())
                .filter(repoStatus -> !repoStatus.getCommit().equals(""))
                .filter(repoStatus -> !SharedPrefsUtil.INSTANCE$
                        .getCommitFromSharedPrefs(ctx).equals(repoStatus.getCommit()))
                .map(repoStatus -> SharedPrefsUtil.INSTANCE$.getCommitFromSharedPrefs(ctx))
                .doOnNext(commit -> Log.i(TAG, "getting update"))
                .flatMap(ucdService::pullRepo);
    }

    /**
     * Creates an observable that either clones the repo if none is present or pulls changes from
     * the remote repo
     */
    @Provides
    public Observable<Repo> createRepoObservable(Observable<Boolean> networkObs,
                                                 UCDService ucdService, Context ctx){
        return createCloneObs(networkObs, ucdService, ctx)
                .mergeWith(createUpdateObs(networkObs, ucdService, ctx))
                .doOnNext(DBTransaction.INSTANCE$::saveRepo)
                .doOnNext(repo -> SharedPrefsUtil.INSTANCE$
                        .saveCommitToSharedPrefs(ctx, repo.getCommit()))
                .doOnNext(repo -> Log.i(TAG, "SP saved"))
                .doOnNext(repo -> Observable.interval(500, TimeUnit.MILLISECONDS)
                        .toBlocking().first())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
