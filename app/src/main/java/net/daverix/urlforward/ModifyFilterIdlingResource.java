package net.daverix.urlforward;

import android.support.test.espresso.IdlingResource;

public class ModifyFilterIdlingResource implements IdlingResource {
    private final String name;
    private boolean isIdle;
    private ResourceCallback callback;

    public ModifyFilterIdlingResource(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;

        if(idle && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}
