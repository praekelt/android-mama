package za.foundation.praekelt.mama;

import android.support.test.espresso.IdlingResource;

import rx.Observable;

/**
 * Idling Resource which waits for an observable to complete
 */
public class ObservableIdlingResource implements IdlingResource{
    ResourceCallback callback;
    boolean done = false;
    Observable<?> obs;

    public ObservableIdlingResource(Observable<?> obs) {
        this.obs = obs;
        this.obs.subscribe(item -> done = true,
                item -> {throw new RuntimeException("Observable resource threw error");},
                () -> done = true);
    }

    @Override
    public String getName() {
        return ObservableIdlingResource.class.getName()+obs.toString();
    }

    @Override
    public boolean isIdleNow() {
        if(done)
            callback.onTransitionToIdle();

        return done;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        callback = resourceCallback;
    }
}
