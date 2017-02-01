/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2016 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daverix.urlforward;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import net.daverix.urlforward.dagger.ActivityComponent;
import net.daverix.urlforward.dagger.ActivityComponentBuilder;
import net.daverix.urlforward.dagger.FragmentComponent;
import net.daverix.urlforward.dagger.FragmentComponentBuilder;
import net.daverix.urlforward.db.DatabaseModule;
import net.daverix.urlforward.filter.FilterModule;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

public class UrlForwarderApplication extends Application {
    private final Object modifyFilterIdlingResourceLock = new Object();
    private ModifyFilterIdlingResource modifyFilterIdlingResource;
    private AppComponent appComponent;

    @Inject
    Map<Class<? extends Activity>, ActivityComponentBuilder> activityComponentBuilders;

    @Inject
    Map<Class<? extends Fragment>, FragmentComponentBuilder> fragmentComponentBuilders;

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

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerUrlForwarderApplication_AppComponent.builder()
                .context(this)
                .build();
        appComponent.inject(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends Activity, C extends ActivityComponent<T>, B extends ActivityComponentBuilder<C>> B getActivityComponentBuilder(Class<T> activity) {
        return (B) activityComponentBuilders.get(activity);
    }

    @SuppressWarnings("unchecked")
    public <T extends Fragment, C extends FragmentComponent<T>, B extends FragmentComponentBuilder<C>> B getFragmentComponentBuilder(Class<T> fragment) {
        return (B) fragmentComponentBuilders.get(fragment);
    }

    @Singleton
    @Component(modules = {
            DatabaseModule.class,
            FilterModule.class,
            FragmentsModule.class,
            ActivitiesModule.class
    })
    public interface AppComponent {
        void inject(UrlForwarderApplication urlForwarderApplication);

        @Component.Builder
        interface Builder {
            @BindsInstance
            Builder context(Context context);

            AppComponent build();
        }
    }
}
