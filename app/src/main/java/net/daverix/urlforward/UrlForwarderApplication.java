package net.daverix.urlforward;

import android.app.Application;

public class UrlForwarderApplication extends Application {
    private ModifyFilterIdlingResource modifyFilterIdlingResource;
    private final Object modifyFilterIdlingResourceLock = new Object();

    public ModifyFilterIdlingResource getModifyFilterIdlingResource() {
        synchronized (modifyFilterIdlingResourceLock) {
            return modifyFilterIdlingResource;
        }
    }

    public void setModifyFilterIdlingResource(ModifyFilterIdlingResource modifyFilterIdlingResource) {
        synchronized (modifyFilterIdlingResourceLock) {
            this.modifyFilterIdlingResource = modifyFilterIdlingResource;
        }
    }
}
